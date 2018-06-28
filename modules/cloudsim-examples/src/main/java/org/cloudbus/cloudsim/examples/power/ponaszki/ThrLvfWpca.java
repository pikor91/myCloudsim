package org.cloudbus.cloudsim.examples.power.ponaszki;

import org.cloudbus.cloudsim.examples.power.planetlab.NonPowerAware;

import java.io.IOException;


public class ThrLvfWpca {

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
		String vmSelectionPolicy = "lvf"; // Maximum Correlation (MC) VM selection policy
		String vmReallocationPolicy = "wpca";
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
