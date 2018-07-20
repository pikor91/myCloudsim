package org.cloudbus.cloudsim.examples.power.ponaszki;

import org.cloudbus.cloudsim.examples.power.planetlab.NonPowerAware;

import java.io.IOException;

/**
 * Created by ponaszki on 2018-05-21.
 */
public class AllDestinationPolicies {
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
        String vmReallocationPolicy = "rr";
        String parameter = "0.8"; // the static utilization threshold
        boolean activeFirst = true;

        System.out.println("Vm Reallocation policy "+ vmReallocationPolicy);
        simulateOneDestination(enableOutput, outputToFile, activeFirst, inputFolder, outputFolder, vmAllocationPolicy, vmReallocationPolicy,  workload, parameter);


    }

    private static void simulateOneDestination(boolean enableOutput, boolean outputToFile, boolean activeFirst, String inputFolder, String outputFolder, String allocationPolicy, String vmReallocationPolicy,  String workload, String parameter) {
        System.out.println("selection policy cth");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "cth", vmReallocationPolicy,
                parameter);

        System.out.println("Selection policy cuv");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "cuv", vmReallocationPolicy,
                parameter);

        System.out.println("Selection policy lvf");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "lvf", vmReallocationPolicy,
                parameter);

        System.out.println("Selection policy maxu");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "maxu", vmReallocationPolicy,
                parameter);

        System.out.println("Selection policy mc");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "mc", vmReallocationPolicy,
                parameter);

        System.out.println("Selection policy minu");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "minu", vmReallocationPolicy,
                parameter);
        System.out.println("Selection policy mmt");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "mmt", vmReallocationPolicy,
                parameter);
        System.out.println("Selection policy rs");
        new PonaszkiRunner(
                enableOutput,
                outputToFile,
                activeFirst,
                inputFolder,
                outputFolder,
                workload,
                allocationPolicy, "rs", vmReallocationPolicy,
                parameter);
    }
}
