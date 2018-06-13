package org.cloudbus.cloudsim.examples.power.ponaszki;

import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.planetlab.NonPowerAware;

import java.io.IOException;

/**
 * Created by ponaszki on 2018-05-21.
 */
public class MakeTable {
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        boolean enableOutput = true;
        boolean outputToFile = false;
        String inputFolder = NonPowerAware.class.getClassLoader().getResource("workload/planetlab").getPath();
        String outputFolder = "output";
        String workload = "20110303"; // PlanetLab workload
        String vmAllocationPolicy = "thr"; // Static Threshold (THR) VM allocation policy
        String vmSelectionPolicy = "mmt"; // Maximum Correlation (MC) VM selection policy
        String vmReallocationPolicy = "lbCpuRatio";
        String parameter = "0.8"; // the static utilization threshold

        Helper.makeStatsTable(outputFolder);
    }

}
