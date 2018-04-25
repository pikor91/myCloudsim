package org.cloudbus.cloudsim.power.hostOverloadDetection;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * Created by ponaszki on 2018-04-20.
 */
public class HostOverUtilisationProcessorLocalRegressionRobust extends  HostOverUtilisationProcessorLocalRegression {

    private double utilizationThreshold = 0.9;


    private HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor;


    public HostOverUtilisationProcessorLocalRegressionRobust(double safetyParameter, double schedulingInterval,  HostOverUtilisationProcessor fallbackHostOverUtilisationProcessor){
        super(safetyParameter, schedulingInterval, fallbackHostOverUtilisationProcessor);
    }

    @Override
    protected double[] getParameterEstimates(double[] utilizationHistoryReversed) {
        return MathUtil.getRobustLoessParameterEstimates(utilizationHistoryReversed);
    }
}
