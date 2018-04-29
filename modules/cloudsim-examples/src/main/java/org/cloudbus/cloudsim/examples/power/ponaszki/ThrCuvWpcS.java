package org.cloudbus.cloudsim.examples.power.ponaszki;

import org.cloudbus.cloudsim.examples.power.planetlab.NonPowerAware;

import java.io.IOException;


public class ThrCuvWpcS {

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
		String workload = "ponaszki"; // PlanetLab workload
		String vmAllocationPolicy = "thr"; // Static Threshold (THR) VM allocation policy
		String vmSelectionPolicy = "cuv"; // Maximum Correlation (MC) VM selection policy
		String vmReallocationPolicy = "wpcs";
		String parameter = "0.8"; // the static utilization threshold

		new PonaszkiRunner(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				vmReallocationPolicy,
				parameter);
	}

}
