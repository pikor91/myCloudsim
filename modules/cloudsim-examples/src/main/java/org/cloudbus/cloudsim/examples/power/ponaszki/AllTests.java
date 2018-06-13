package org.cloudbus.cloudsim.examples.power.ponaszki;

import org.cloudbus.cloudsim.examples.power.planetlab.NonPowerAware;

import java.io.IOException;

/**
 * Created by ponaszki on 2018-05-21.
 */
public class AllTests {
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

        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "cth", workload, parameter);
//        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "cuv", workload, parameter);
        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "lvf", workload, parameter);
        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "maxu", workload, parameter);
        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "minu", workload, parameter);
        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "mc", workload, parameter);
        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "mmt", workload, parameter);
        simulateOneSelection(enableOutput, outputToFile, inputFolder, outputFolder, vmAllocationPolicy, "rs", workload, parameter);

    }

    private static void simulateOneSelection(boolean enableOutput, boolean outputToFile, String inputFolder, String outputFolder, String allocationPolicy, String vmSelectionPolicy, String workload, String parameter) {
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "lbCpuRatio",
                parameter);
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "pck",
                parameter);
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "rr",
                parameter);
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "stp",
                parameter);
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "wpc",
                parameter);
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "wpca",
                parameter);
    }
}
