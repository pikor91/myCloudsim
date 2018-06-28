/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power.hostOverloadDetection;

import com.google.common.collect.Iterators;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

import java.util.*;

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
public class PowerVmAllocationPolicyMigrationStaticThresholdReallocationPolicyRoundRobin extends PowerVmAllocationPolicyMigrationStaticThreshold {

	/** The static host CPU utilization threshold to detect over utilization.
         * It is a percentage value from 0 to 1
         * that can be changed when creating an instance of the class. */
	private double utilizationThreshold = 0.9;

	private Iterator<PowerHost> iterator;
	/**
	 * Instantiates a new PowerVmAllocationPolicyMigrationStaticThreshold.
	 *  @param hostList the host list
	 * @param vmSelectionPolicy the vm selection policy
	 * @param utilizationThreshold the utilization threshold
	 * @param activeFirst
	 */
	public PowerVmAllocationPolicyMigrationStaticThresholdReallocationPolicyRoundRobin(
			List<? extends Host> hostList,
			PowerVmSelectionPolicy vmSelectionPolicy,
			double utilizationThreshold, boolean activeFirst) {
		super(hostList, vmSelectionPolicy, utilizationThreshold, activeFirst);
		setUtilizationThreshold(utilizationThreshold);
		iterator = Iterators.cycle((Iterable<PowerHost>) hostList);
		setAvtiveFirst(activeFirst);
	}


	/**
	 * Gets the utilization threshold.
	 * 
	 * @return the utilization threshold
	 */
	protected double getUtilizationThreshold() {
		return utilizationThreshold;
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

	public PowerHost findHostForVmFromPassed(Vm vm, List<? extends Host> destHosts, Set<? extends Host> excludedHosts) {

		double minPower = Double.MAX_VALUE;
		PowerHost allocatedHost = null;
		List<PowerHost> hostList = (List<PowerHost>) destHosts;

		PowerHost host;
		int count = 0;
		while ((host = getNextHost()) != null) {
			if(count>=hostList.size()){
				break;
			}
			count++;

			if (excludedHosts.contains(host)) {
				continue;
			}
			if (host.isSuitableForVm(vm)) {
				if (getUtilizationOfCpuMips(host) != 0 && isHostOverUtilizedAfterAllocation(host, vm)) {
					continue;
				}

				try {
					double powerAfterAllocation = getPowerAfterAllocation(host, vm);
					if (powerAfterAllocation != -1) {
						double powerDiff = powerAfterAllocation - host.getPower();
						allocatedHost = host;
						break;
					}
				} catch (Exception e) {
				}
			}else{
				Log.print("Host is not suitable for vm");
			}

		}
		return allocatedHost;
	}

	private PowerHost getNextHost() {
		return iterator.next();
	}


	private Comparator<PowerHost> getPowerHostComparatorInstance() {
		return new Comparator<PowerHost>() {
			@Override
			public int compare(PowerHost o1, PowerHost o2) {
				if (o1 == null || o2 == null) {
					throw new NullPointerException("One of powergosts is null");
				}
				if (o1.getId() > o2.getId()) {
					return 1;
				} else if (o1.getId() < o2.getId()) {
					return -1;
				} else {
					return 0;
				}
			}

		};
	}
}
