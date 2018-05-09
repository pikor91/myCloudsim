/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2011, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;

import org.cloudbus.cloudsim.power.HostState;

/**
 * Stores historic data about a host.
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 2.1.2
 */
public class HostStateAwareHistoryEntry {

	/** The time. */
	private double time;

	/** The allocated mips. */
	private HostState startState;

	/** The requested mips. */
	private HostState destinationState;

	private HostState afterTransitionState;

	/** Indicates if the host was active in the indicated time.
         * @see #time
         */
	private double transitionEndTime ;

	public HostStateAwareHistoryEntry(double time, HostState startState, HostState destState, HostState stateAfterTransitionEnd, double transitionEndTime ) {
		setTime(time);
		setStartState(startState);
		setDestinationState(destState);
		setAfterTransitionState(stateAfterTransitionEnd);
		setTransitionEndTime(transitionEndTime);
	}


	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public HostState getStartState() {
		return startState;
	}

	public void setStartState(HostState startState) {
		this.startState = startState;
	}

	public HostState getDestinationState() {
		return destinationState;
	}

	public void setDestinationState(HostState destinationState) {
		this.destinationState = destinationState;
	}

	public double getTransitionEndTime() {
		return transitionEndTime;
	}

	public void setTransitionEndTime(double transitionEndTime) {
		this.transitionEndTime = transitionEndTime;
	}


	public HostState getAfterTransitionState() {
		return afterTransitionState;
	}

	public void setAfterTransitionState(HostState afterTransitionState) {
		this.afterTransitionState = afterTransitionState;
	}

}
