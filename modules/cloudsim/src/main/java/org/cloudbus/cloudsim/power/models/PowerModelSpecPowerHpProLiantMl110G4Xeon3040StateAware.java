/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power.models;

import org.cloudbus.cloudsim.power.HostState;

import java.util.Map;

/**
 * The power model of an HP ProLiant ML110 G4 (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB).<br/>
 * <a href="http://www.spec.org/power_ssj2008/results/res2011q1/power_ssj2008-20110127-00342.html">
 * http://www.spec.org/power_ssj2008/results/res2011q1/power_ssj2008-20110127-00342.html</a>
 * 
 * <br/>If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:<br/>
 * 
 * <ul>
 * <li><a href="http://dx.doi.org/10.1002/cpe.1867">Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012</a>
 * </ul>
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 3.0
 */
public class PowerModelSpecPowerHpProLiantMl110G4Xeon3040StateAware extends PowerModelSpecPowerHpProLiantMl110G4Xeon3040 {

	private final int[] transitionTimes = {90, 70};

	@Override
	public int getTransitionTime(HostState startState, HostState destinationState) {
		if(startState.equals(destinationState)) {
			return 0;
		}else if(startState.equals(HostState.INACTIVE) && destinationState.equals(HostState.ACTIVE)){
				return transitionTimes[0];
		}else if (startState.equals(HostState.ACTIVE) && destinationState.equals(HostState.INACTIVE)){
				return transitionTimes[1];
		}
		return 0;
	}
}
