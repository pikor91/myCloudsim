/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power.hostOverloadDetection;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

import java.util.List;
import java.util.Set;

/**
 * A VM allocation policy that uses a Static CPU utilization Threshold (THR) to detect host over
 * utilization.
 * 
 * <br/>If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:<br/>
 * 
 * <ul>
 * <li><a href="http://dx.doi.org/10.1002/cpe.1867">Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012</a>
 * </ul>
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 3.0
 */
public class PowerVmAllocationPolicyMigrationStaticThresholdReallocationPolicyWattsPerCoreAbsolute extends PowerVmAllocationPolicyMigrationStaticThreshold {

	/** The static host CPU utilization threshold to detect over utilization.
         * It is a percentage value from 0 to 1
         * that can be changed when creating an instance of the class. */
	private double utilizationThreshold = 0.9;

	/**
	 * Instantiates a new PowerVmAllocationPolicyMigrationStaticThreshold.
	 *  @param hostList the host list
	 * @param vmSelectionPolicy the vm selection policy
	 * @param utilizationThreshold the utilization threshold
	 * @param activeFirst
	 */
	public PowerVmAllocationPolicyMigrationStaticThresholdReallocationPolicyWattsPerCoreAbsolute(
			List<? extends Host> hostList,
			PowerVmSelectionPolicy vmSelectionPolicy,
			double utilizationThreshold, boolean activeFirst) {
		super(hostList, vmSelectionPolicy, utilizationThreshold, activeFirst);
		setUtilizationThreshold(utilizationThreshold);
		setAvtiveFirst(activeFirst);
	}

	@Override
	public PowerHost findHostForVm(Vm vm, Set<? extends Host> excludedHosts) {
		if(!isAvtiveFirst()){
			PowerHost hostForVmAll = findHostForVmFromPassed(vm, getHostList(), excludedHosts);
			return hostForVmAll;
		}else{
			List<? extends Host> activeHosts = getActiveHosts();
			PowerHost hostForVmActive = findHostForVmFromPassed(vm, activeHosts, excludedHosts);
			if(hostForVmActive==null){
				List<? extends Host> inactiveHosts = getInactiveHosts();
				hostForVmActive = findHostForVmFromPassed(vm, inactiveHosts, excludedHosts);
			}
			return hostForVmActive;
		}
	}

	public PowerHost findHostForVmFromPassed(Vm vm, List<? extends Host> possibleDestinationHosts, Set<? extends Host> excludedHosts) {
		double minPower = Double.MAX_VALUE;
		PowerHost allocatedHost = null;
		PowerHost sourceHost = (PowerHost) vm.getHost();
		if(sourceHost == null){
			Log.printConcatLine("SourceHost is null. Initial VM allocation.");
		}
		for (Host h : possibleDestinationHosts) {
			PowerHost host = (PowerHost) h;
			if (excludedHosts.contains(host)) {
				continue;
			}
			if (host.isSuitableForVm(vm)) {
				if (getUtilizationOfCpuMips(host) != 0 && isHostOverUtilizedAfterAllocation(host, vm)) {
					continue;
				}

				try {
					double destPowerAfterAllocation = getPowerAfterAllocation(host, vm);
					double sourcePowerAfterDeallocation = getPowerAfterDeallocation(sourceHost, vm);
					if (destPowerAfterAllocation != -1 && sourcePowerAfterDeallocation != -1) {
						double powerRaise = destPowerAfterAllocation - host.getPower();
						double powerDrop=0;
						if(sourceHost != null) {
							powerDrop = sourceHost.getPower() - sourcePowerAfterDeallocation;
						}
						double powerDiff = powerRaise - powerDrop;
						if (powerDiff < minPower) {
							minPower = powerDiff;
							allocatedHost = host;
						}
					}
				} catch (Exception e) {
				}
			}
		}
		return allocatedHost;
	}
}
