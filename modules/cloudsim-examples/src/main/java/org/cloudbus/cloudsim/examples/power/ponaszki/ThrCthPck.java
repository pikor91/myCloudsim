package org.cloudbus.cloudsim.examples.power.ponaszki;

import org.cloudbus.cloudsim.examples.power.planetlab.NonPowerAware;

import java.io.IOException;


public class ThrCthPck {

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
		String vmSelectionPolicy = "cth"; // Maximum Correlation (MC) VM selection policy
		String vmReallocationPolicy = "pck";
		String parameter = "0.8"; // the static utilization threshold
		boolean activeFirst = false;

		new PonaszkiRunner(
				enableOutput,
				outputToFile,
                activeFirst, inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				vmReallocationPolicy,
				parameter);
	}

}
