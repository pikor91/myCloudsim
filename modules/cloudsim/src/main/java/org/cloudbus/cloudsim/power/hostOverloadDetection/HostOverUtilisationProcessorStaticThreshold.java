package org.cloudbus.cloudsim.power.hostOverloadDetection;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * Created by ponaszki on 2018-04-20.
 */
public class HostOverUtilisationProcessorStaticThreshold extends  HostOverUtilisationProcessor {

    private double utilizationThreshold = 0.9;


    private HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor;


    public HostOverUtilisationProcessorStaticThreshold(double utilizationThreshold){
        this.utilizationThreshold = utilizationThreshold;
        this.fallbackHostOverUtilisationProcessor = fallbackHostOverUtilisationProcessor;
    }

    public boolean isHostOverUtilized(PowerHost host, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract){
        powerVmAllocationPolicyMigrationAbstract.addHistoryEntry(host, getUtilizationThreshold());
        double utilization = getCurrentUtilizationOfHost(host);
        return utilization > getUtilizationThreshold();
    }

    @Override
    public double getHostUtilizationThreshold(PowerHost host) {
        return utilizationThreshold;
    }

    public double countCurrentUtilisation(PowerHost host){
        double totalRequestedMips = 0;
        for (Vm vm : host.getVmList()) {
            totalRequestedMips += vm.getCurrentRequestedTotalMips();
        }
        double utilization = totalRequestedMips / host.getTotalMips();
        return utilization;
    }

    /**
     * Gets the host CPU utilization percentage IQR.
     *
     * @param host the host
     * @return the host CPU utilization percentage IQR
     */
    protected double getHostUtilizationThreshold(PowerHostUtilizationHistory host) throws IllegalArgumentException {
        double[] data = host.getUtilizationHistory();
        if (MathUtil.countNonZeroBeginning(data) >= 12) { // 12 has been suggested as a safe value
            return MathUtil.iqr(data);
        }
        throw new IllegalArgumentException();
    }

    public double getUtilizationThreshold() {
        return utilizationThreshold;
    }

}
