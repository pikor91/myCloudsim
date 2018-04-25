package org.cloudbus.cloudsim.power.hostOverloadDetection;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * Created by ponaszki on 2018-04-20.
 */
public class HostOverUtilisationProcessorLocalRegression extends  HostOverUtilisationProcessor {

    private double safetyParameter = 0.9;

    private HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor;
    private double schedulingInterval;


    public HostOverUtilisationProcessor getFallbackHostOverUtilisationProcessor() {
        return fallbackHostOverUtilisationProcessor;
    }

    public HostOverUtilisationProcessorLocalRegression(double safetyParameter, double schedulingInterval,  HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor){
        this.safetyParameter = safetyParameter;
        this.fallbackHostOverUtilisationProcessor = fallbackHostOverUtilisationProcessor;
        this.schedulingInterval = schedulingInterval;
    }

    public boolean isHostOverUtilized(PowerHost host, PowerVmAllocationPolicyMigrationAbstract powerVmAllocationPolicyMigrationAbstract){
        double predictedUtilization = getHostUtilizationThreshold(host);
        powerVmAllocationPolicyMigrationAbstract.addHistoryEntry(host, predictedUtilization);
        return predictedUtilization >= 1;
    }

    /**
     * Gets the host CPU utilization percentage IQR.
     *
     * @param host the host
     * @return the host CPU utilization percentage IQR
     */
    public double getHostUtilizationThreshold(PowerHost host){
        PowerHostUtilizationHistory _host = (PowerHostUtilizationHistory) host;
        double[] utilizationHistory = _host.getUtilizationHistory();
        int length = 10; // we use 10 to make the regression responsive enough to latest values
        if (utilizationHistory.length < length) {
            return getFallbackHostOverUtilisationProcessor().getHostUtilizationThreshold(host);
        }
        double[] utilizationHistoryReversed = new double[length];
        for (int i = 0; i < length; i++) {
            utilizationHistoryReversed[i] = utilizationHistory[length - i - 1];
        }
        double[] estimates = null;
        try {
            estimates = getParameterEstimates(utilizationHistoryReversed);
        } catch (IllegalArgumentException e) {
        }
        double migrationIntervals = Math.ceil(getMaximumVmMigrationTime(_host) / getSchedulingInterval());
        double predictedUtilization = estimates[0] + estimates[1] * (length + migrationIntervals);
        predictedUtilization *= getSafetyParameter();
        return predictedUtilization;
    }

    /**
     * Gets the maximum vm migration time.
     *
     * @param host the host
     * @return the maximum vm migration time
     */
    protected double getMaximumVmMigrationTime(PowerHost host) {
        int maxRam = Integer.MIN_VALUE;
        for (Vm vm : host.getVmList()) {
            int ram = vm.getRam();
            if (ram > maxRam) {
                maxRam = ram;
            }
        }
        return maxRam / ((double) host.getBw() / (2 * 8000));
    }

    /**
     * Gets the scheduling interval.
     *
     * @return the scheduling interval
     */
    protected double getSchedulingInterval() {
        return schedulingInterval;
    }

    public double getSafetyParameter() {
        return safetyParameter;
    }

    /**
     * Gets utilization estimates.
     *
     * @param utilizationHistoryReversed the utilization history in reverse order
     * @return the utilization estimates
     */
    protected double[] getParameterEstimates(double[] utilizationHistoryReversed) {
        return MathUtil.getLoessParameterEstimates(utilizationHistoryReversed);
    }
}
