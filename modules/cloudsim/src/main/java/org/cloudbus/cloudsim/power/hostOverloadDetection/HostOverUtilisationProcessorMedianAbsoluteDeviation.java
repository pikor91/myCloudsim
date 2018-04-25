package org.cloudbus.cloudsim.power.hostOverloadDetection;

import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * Created by ponaszki on 2018-04-20.
 */
public class HostOverUtilisationProcessorMedianAbsoluteDeviation extends  HostOverUtilisationProcessor {

    private double safetyParameter = 0.9;

    private HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor;


    public HostOverUtilisationProcessor getFallbackHostOverUtilisationProcessor() {
        return fallbackHostOverUtilisationProcessor;
    }

    public HostOverUtilisationProcessorMedianAbsoluteDeviation(double safetyParameter, HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor){
        this.safetyParameter = safetyParameter;
        this.fallbackHostOverUtilisationProcessor = fallbackHostOverUtilisationProcessor;
    }

    public boolean isHostOverUtilized(PowerHost host, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract){
        double upperThreshold = getHostUtilizationThreshold(host);
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
        PowerHostUtilizationHistory _host = (PowerHostUtilizationHistory) host;
        double[] data = _host.getUtilizationHistory();
        if (MathUtil.countNonZeroBeginning(data) >= 12) { // 12 has been suggested as a safe value
            return MathUtil.mad(data);
        }
        throw new IllegalArgumentException();
    }


    public double getSafetyParameter() {
        return safetyParameter;
    }

}
