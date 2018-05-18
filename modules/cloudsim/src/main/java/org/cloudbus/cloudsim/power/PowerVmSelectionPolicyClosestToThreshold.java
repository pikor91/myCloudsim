/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.hostOverloadDetection.PowerVmAllocationPolicyMigrationAbstract;

import java.util.List;


public class PowerVmSelectionPolicyClosestToThreshold extends PowerVmSelectionPolicy {

	private double threshold = 0.9;

	@Override
	public Vm getVmToMigrate(PowerHost host, List<? extends Host> hostList, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract) {
		List<PowerVm> migratableVms = getMigratableVms(host);
		if (migratableVms.isEmpty()) {
			return null;
		}
		//counting difference between threshold mips  and total allocated mips
		//allocated mips should always be greater. because this policy is used during host overload scenario
		double thresholdMips = threshold*host.getTotalMips();
		double totalAllocatedMips = host.getTotalAllocatedMips();
		double difference = 0.0;
		if(thresholdMips >= totalAllocatedMips){
			difference = thresholdMips - totalAllocatedMips;
		}else{
			difference = totalAllocatedMips - thresholdMips;
		}

		Vm vmToMigrate = null;
		double minMetric = Double.MAX_VALUE;
		for (Vm vm : migratableVms) {
			if (vm.isInMigration()) {
				continue;
			}

			double vmMips = vm.getTotalUtilizationOfCpuMips(CloudSim.clock());
			double metric = vmMips - difference;
			if(metric > 0) {
				if (metric < minMetric) {
					minMetric = metric;
					vmToMigrate = vm;
				}
			}
		}

		if(vmToMigrate == null){
			for (Vm vm : migratableVms) {
				if (vm.isInMigration()) {
					continue;
				}

				double vmMips = vm.getTotalUtilizationOfCpuMips(CloudSim.clock());
				double metric = vmMips - difference;
				if(metric < 0) {
					if (Math.abs(metric) < minMetric) {
						minMetric = Math.abs(metric);
						vmToMigrate = vm;
					}
				}
			}
		}
		return vmToMigrate;
	}
}
