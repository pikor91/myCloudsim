/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.hostOverloadDetection.PowerVmAllocationPolicyMigrationAbstract;

import java.util.*;


public class PowerVmSelectionPolicyCPUUtilizationVariance extends PowerVmSelectionPolicy {

	Map<Host, List<List<Double>>> vuVectors = new HashMap<>();

	@Override
	public Vm getVmToMigrate(PowerHost host, List<? extends Host> hostList, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract) {
		List<PowerVm> migratableVms = getMigratableVms(host);
		if (migratableVms.isEmpty()) {
			return null;
		}
		List<List<Double>> VU = null;
		Vm vmToMigrate = null;

			int numberOfVms = migratableVms.size();
			VU = new LinkedList<>();

			for (int v = 0; v < numberOfVms; v++) {
				PowerVm powerVm = migratableVms.get(v);
				//określenie ilości pasujących do migracji potencjalnych hostów
				List<? extends Host> avaiableDestinationHosts = getAvaiableDestinationHosts(hostList, powerVm, powerVmAllocationPolicyMigrationAbstract);
				int numberOfHosts = avaiableDestinationHosts.size();
				VU.add(v, new LinkedList<>());
				for (int h = 0; h < numberOfHosts; h++) {
					Host possibleDestinationHost = avaiableDestinationHosts.get(h);
					double utilizationOfPossibleDestinationAfterAllocation = getUtilizationAfterAllocation(possibleDestinationHost, powerVm, powerVmAllocationPolicyMigrationAbstract);
					double meanUtilization = getMeanUtilizationAfterAllocation(avaiableDestinationHosts, utilizationOfPossibleDestinationAfterAllocation, possibleDestinationHost);
					double variance = 0.0;
					for (int h2 = 0; h2 < numberOfHosts; h2++) {
						PowerHost pp = (PowerHost) avaiableDestinationHosts.get(h2);
						double utilizationOfCpuCurrentHost = 0;
						if (pp.getId() == possibleDestinationHost.getId()) {
							utilizationOfCpuCurrentHost = utilizationOfPossibleDestinationAfterAllocation;
						} else {
							utilizationOfCpuCurrentHost = pp.getUtilizationOfCpu();
						}

						double distance = utilizationOfCpuCurrentHost - meanUtilization;
						double squaredDistance = Math.pow(distance, 2);
						variance = variance + squaredDistance;
					}
					variance = variance / numberOfHosts;
					VU.get(v).add(h, variance);
				}
			}


		int vMin = -1, hMin = -1;
		double minVariance= Double.MAX_VALUE;
		for(int v =0; v < VU.size(); v++){
			List<Double> variancesForVm = VU.get(v);
			for(int h =0; h <  variancesForVm.size(); h++){
				Double variance = variancesForVm.get(h);
				if(variance< minVariance){
					minVariance = variance;
					vMin = v;
					hMin = h;
				}
			}
		}

		vmToMigrate = migratableVms.get(vMin);
		return vmToMigrate;
	}

	private double getMeanUtilizationAfterAllocation(List<? extends Host> avaiableDestinationHosts, double utilizationOfPossibleDestinationHostAfterAllocation, Host possibleDestinationHost) {
		if(avaiableDestinationHosts == null || avaiableDestinationHosts.isEmpty())
			return 0;
		double mean = 0;
		for(int i = 0; i < avaiableDestinationHosts.size(); i++){
			Host currentHost = avaiableDestinationHosts.get(i);
			if(currentHost.getId() == possibleDestinationHost.getId()) {
				mean += utilizationOfPossibleDestinationHostAfterAllocation;
			}else {
				PowerHost ph = (PowerHost) currentHost;
				mean += ph.getUtilizationOfCpu();
			}
		}

		mean = mean/(avaiableDestinationHosts.size());
		return mean;

	}

	private double getUtilizationAfterAllocation(Host possibleDestinationHost, PowerVm powerVm, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract) {
		PowerHost ph = (PowerHost) possibleDestinationHost;
		double result = powerVmAllocationPolicyMigrationAbstract.getMaxUtilizationAfterAllocation(ph, powerVm);

		return result;
	}

	private List<? extends Host> getAvaiableDestinationHosts(List<? extends Host> hostList, PowerVm powerVm, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract) {
		List<Host> resultList = new ArrayList<>();
		if (powerVmAllocationPolicyMigrationAbstract == null){
			return resultList;
		}
		for(Host h : hostList){
			PowerHost _ph = (PowerHost) h;
			if(!isHostOverUtilizedAfterAllocation(_ph, powerVm, powerVmAllocationPolicyMigrationAbstract) &&_ph.isActive()){
				resultList.add(h);
			}
		}
		return resultList;
	}

	/**
	 * Checks if a host will be over utilized after placing of a candidate VM.
	 *
	 * @param host the host to verify
	 * @param vm the candidate vm
	 * @param powerVmAllocationPolicyMigrationAbstract
	 * @return true, if the host will be over utilized after VM placement; false otherwise
	 */
	protected boolean isHostOverUtilizedAfterAllocation(PowerHost host, Vm vm, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract) {
		boolean isHostOverUtilizedAfterAllocation = true;
		if (host.vmCreate(vm)) {
			isHostOverUtilizedAfterAllocation = powerVmAllocationPolicyMigrationAbstract.isHostOverUtilized(host);
			host.vmDestroy(vm);
		}
		return isHostOverUtilizedAfterAllocation;
	}

	public void resetPolicyForHost(PowerHostUtilizationHistory host) {
		vuVectors.remove(host);
	}
}
