package org.cloudbus.cloudsim.power.hostOverloadDetection;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * Created by ponaszki on 2018-04-20.
 */
public abstract class HostOverUtilisationProcessorInterQuartileRange extends  HostOverUtilisationProcessor {

    private double safetyParameter = 0.9;

    private HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor;


    public HostOverUtilisationProcessor getFallbackHostOverUtilisationProcessor() {
        return fallbackHostOverUtilisationProcessor;
    }

    public HostOverUtilisationProcessorInterQuartileRange(double safetyParameter, HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor){
        this.safetyParameter = safetyParameter;
        this.fallbackHostOverUtilisationProcessor = fallbackHostOverUtilisationProcessor;
    }

    public boolean isHostOverUtilized(PowerHost host, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract){
        double upperThreshold = 0;

        upperThreshold = getHostUtilizationThreshold(host);
        powerVmAllocationPolicyMigrationAbstract.addHistoryEntry(host, upperThreshold);

        double utilization = getCurrentUtilizationOfHost(host);
        return utilization > upperThreshold;
    }

    /**
     * Gets the host CPU utilization percentage IQR.
     *
     * @param host the host
     * @return the host CPU utilization percentage IQR
     */
    public double getHostUtilizationThreshold(PowerHost host) throws IllegalArgumentException {
        double upperThreshold = 0;
        PowerHostUtilizationHistory _host = (PowerHostUtilizationHistory) host;
        try {
            upperThreshold = 1 - getSafetyParameter() * getHostUtilizationThreshold(_host);
        } catch (IllegalArgumentException e) {
            return getFallbackHostOverUtilisationProcessor().getHostUtilizationThreshold(host);
        }
        return upperThreshold;
    }

    public double getSafetyParameter() {
        return safetyParameter;
    }

}
