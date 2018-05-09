package org.cloudbus.cloudsim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Defines the resource utilization model based on 
 * a <a href="https://www.planet-lab.org">PlanetLab</a>
 * datacenter trace file.
 */
public class UtilizationModelPonaszkiInMemory extends UtilizationModelPlanetLabInMemory {

	/** The scheduling interval. */
	private double schedulingInterval;
	/**
	 * Instantiates a new PlanetLab resource utilization model from a trace file.
	 *
	 * @param inputPath The path of a PlanetLab datacenter trace.
         * @param schedulingInterval
	 * @throws NumberFormatException the number format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public UtilizationModelPonaszkiInMemory(String inputPath, double schedulingInterval)
			throws NumberFormatException,
			IOException {

		int fileSize = 289;
		data = new double[2*fileSize];
		setSchedulingInterval(schedulingInterval);
		BufferedReader input = new BufferedReader(new FileReader(inputPath));
		int n = fileSize;
		for (int i = 0; i < n - 1; i++) {
			data[i] = Integer.valueOf(input.readLine()) / 100.0;
		}
		int counter=0;
		for (int i = n-1; i < 2*n - 1; i++) {
			data[i] = data[counter++] ;
		}
		data[2*n - 1] = data[2*n - 2];
		input.close();
	}

	/**
	 * Instantiates a new PlanetLab resource utilization model with variable data samples
         * from a trace file.
	 *
	 * @param inputPath The path of a PlanetLab datacenter trace.
	 * @param dataSamples number of samples in the file
	 * @throws NumberFormatException the number format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public UtilizationModelPonaszkiInMemory(String inputPath, double schedulingInterval, int dataSamples)
			throws NumberFormatException,
			IOException {
		int fileSize = 289;
		data = new double[2*fileSize];
		setSchedulingInterval(schedulingInterval);
		BufferedReader input = new BufferedReader(new FileReader(inputPath));
		int n = fileSize;
		for (int i = 0; i < n - 1; i++) {
			data[i] = Integer.valueOf(input.readLine()) / 100.0;
		}
		int counter=0;
		for (int i = n-1; i < 2*n - 1; i++) {
			data[i] = data[counter++] ;
		}
		data[2*n - 1] = data[2*n - 2];
		input.close();
	}
}
