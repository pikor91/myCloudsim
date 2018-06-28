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
        boolean enableOutput = false;
        boolean outputToFile = true;
        String inputFolder = NonPowerAware.class.getClassLoader().getResource("workload/planetlab").getPath();
        String outputFolder = "output";
        String workload = "20110303"; // PlanetLab workload
        String vmAllocationPolicy = "thr"; // Static Threshold (THR) VM allocation policy
        String vmSelectionPolicy = "mmt"; // Maximum Correlation (MC) VM selection policy
        String vmReallocationPolicy = "lbCpuRatio";
        String parameter = "0.8"; // the static utilization threshold
        boolean activeFirst = true;

        System.out.println("Vm selection policy cth");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "cth", workload, parameter);

        System.out.println("Vm selection policy cuv");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "cuv", workload, parameter);

        System.out.println("Vm selection policy lvf");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "lvf", workload, parameter);

        System.out.println("Vm selection policy maxu");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "maxu", workload, parameter);

        System.out.println("Vm selection policy minu");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "minu", workload, parameter);

        System.out.println("Vm selection policy mc");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "mc", workload, parameter);

        System.out.println("Vm selection policy mmt");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "mmt", workload, parameter);

        System.out.println("Vm selection policy rs");
        simulateOneSelection(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, "rs", workload, parameter);

    }

    private static void simulateOneSelection(boolean enableOutput, boolean outputToFile, boolean activeFirst, String inputFolder, String outputFolder, String allocationPolicy, String vmSelectionPolicy, String workload, String parameter) {
        System.out.println("Destination host selection policy lbCpuRatio");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "lbCpuRatio",
                parameter);

        System.out.println("Destination host selection policy pck");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "pck",
                parameter);

        System.out.println("Destination host selection policy rr");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "rr",
                parameter);

        System.out.println("Destination host selection policy stp");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "stp",
                parameter);

        System.out.println("Destination host selection policy wpc");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "wpc",
                parameter);

        System.out.println("Destination host selection policy wpca");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, vmSelectionPolicy, "wpca",
                parameter);
    }
}
