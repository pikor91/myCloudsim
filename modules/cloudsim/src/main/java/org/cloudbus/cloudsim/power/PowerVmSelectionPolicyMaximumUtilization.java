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


public class PowerVmSelectionPolicyMaximumUtilization extends PowerVmSelectionPolicy {
	@Override
	public Vm getVmToMigrate(PowerHost host, List<? extends Host> hostList, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract) {
		List<PowerVm> migratableVms = getMigratableVms(host);
		if (migratableVms.isEmpty()) {
			return null;
		}
		Vm vmToMigrate = null;
		double minMetric = Double.MIN_VALUE;
		for (Vm vm : migratableVms) {
			if (vm.isInMigration()) {
				continue;
			}
			double metric = vm.getTotalUtilizationOfCpuMips(CloudSim.clock()) / vm.getMips();
			if (metric > minMetric) {
				minMetric = metric;
				vmToMigrate = vm;
			}
		}
		return vmToMigrate;
	}

}
