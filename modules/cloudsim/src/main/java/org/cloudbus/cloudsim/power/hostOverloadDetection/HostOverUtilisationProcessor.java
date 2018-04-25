package org.cloudbus.cloudsim.power.hostOverloadDetection;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;

/**
 * Created by ponaszki on 2018-04-20.
 */
public abstract class HostOverUtilisationProcessor {

    public boolean isHostOverUtilized(PowerHost host, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract){
        double countedThreshold = this.getHostUtilizationThreshold(host);
        powerVmAllocationPolicyMigrationAbstract.addHistoryEntry(host, countedThreshold);
        double currentUtilization = this.getCurrentUtilizationOfHost(host);
        return currentUtilization > countedThreshold;
    }

    boolean isHostOverutilizedAfterAllocation(PowerHost host, Vm vm, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract){
        boolean isHostOverUtilizedAfterAllocation = true;
        if (host.vmCreate(vm)) {
            isHostOverUtilizedAfterAllocation = isHostOverUtilized(host, powerVmAllocationPolicyMigrationAbstract);
            host.vmDestroy(vm);
        }
        return isHostOverUtilizedAfterAllocation;
    }


    public double getCurrentUtilizationOfHost(PowerHost host) {
        double totalRequestedMips = 0;
        for (Vm vm : host.getVmList()) {
            totalRequestedMips += vm.getCurrentRequestedTotalMips();
        }
        return totalRequestedMips / host.getTotalMips();
    }

    public abstract double getHostUtilizationThreshold(PowerHost host);

}
