/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPower;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.LinkedList;
import java.util.List;

/**
 * PowerHost class enables simulation of power-aware hosts.
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
 * @since CloudSim Toolkit 2.0
 */
public class PowerHostStateAware extends PowerHostUtilizationHistory {


	private HostState currentState;
	private HostState afterTransitionState;

	private boolean duringTransition;
	private double transitionEndTime;
	private boolean underUtilized;
	List<HostStateAwareHistoryEntry> statelog = new LinkedList<>();
	/**
	 * Instantiates a new PowerHost.
	 *
	 * @param id the id of the host
	 * @param ramProvisioner the ram provisioner
	 * @param bwProvisioner the bw provisioner
	 * @param storage the storage capacity
	 * @param peList the host's PEs list
	 * @param vmScheduler the VM scheduler
	 */
	public PowerHostStateAware(
			int id,
			RamProvisioner ramProvisioner,
			BwProvisioner bwProvisioner,
			long storage,
			List<? extends Pe> peList,
			VmScheduler vmScheduler,
			PowerModelSpecPower powerModel,
			HostState hostState) {
		super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler, powerModel);
		currentState = hostState;
		duringTransition = false;
	}

	private boolean changeState(HostState destinationState){
		if(!duringTransition){
			if(destinationState.equals(this.currentState)){
				Log.printConcatLine("Destination currentState is the same as previous: ", destinationState);
			}
			currentState = destinationState;
			return true;
		}else{
			Log.printConcatLine("Could not change currentState. Host already in transition");
		}
		return false;
	}

	@Override
	public double getEnergyLinearInterpolation(double fromUtilization, double toUtilization, double time) {
		if(isActive()) {
			return super.getEnergyLinearInterpolation(fromUtilization, toUtilization, time);
		}else if(HostState.INACTIVE.equals(currentState) && !duringTransition){
			return 0;
		}else {
			return super.getEnergyLinearInterpolation(1.0, 1.0, time);
		}
	}

	@Override
	public PowerModelSpecPower getPowerModel() {
		return (PowerModelSpecPower) super.getPowerModel();
	}

	public boolean startTransition(HostState destinationState , double currentTime){
		if(this.currentState.equals(destinationState)){
			Log.printConcatLine("StartAnd dest currentState are the zame on host #", this.getId(), "can not start a transition;");
		}
		if(!duringTransition) {
			duringTransition = true;
			transitionEndTime = currentTime + getPowerModel().getTransitionTime(this.getCurrentState(), destinationState);
			afterTransitionState = destinationState;
			addChangeStateEntry(currentTime, this.currentState, null,  afterTransitionState, transitionEndTime);
			underUtilized=false;
			return true;
		}else{
			Log.printConcatLine("Can not start transition to currentState", destinationState ,"at time ", currentTime,"Host #", this.getId(), "already in transition.");
		}
		return false;
	}



	public boolean endTransition(double currentTime){
		addChangeStateEntry(currentTime, this.afterTransitionState, null,  null, 0.0);
		if(currentTime>=transitionEndTime && duringTransition == true){
			duringTransition = false;
			transitionEndTime = 0.0;
			changeState(afterTransitionState);
			afterTransitionState = null;

			return true;
		}else{
			Log.printConcatLine("Cannot end transition on host #", this.getId(), " TransitionEndTime ",transitionEndTime," not reached. Current time: ", currentTime);
		}
		return false;
	}
	public boolean isActive(){
		return HostState.ACTIVE.equals(currentState) && !isDuringTransition();
	}


	public boolean isInactive(){
		return HostState.INACTIVE.equals(currentState) || isDuringTransition();
	}


	public HostState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(HostState currentState) {
		this.currentState = currentState;
	}

    public void setUnderUtilised(boolean underUtilised) {
		this.underUtilized = underUtilised;
    }

	public boolean isUnderUtilized() {
		return underUtilized;
	}

	public boolean isDuringTransition() {
		return duringTransition;
	}

	public void setDuringTransition(boolean duringTransition) {
		this.duringTransition = duringTransition;
	}


	public double getTransitionEndTime() {
		return transitionEndTime;
	}

	private void addChangeStateEntry(double currentTime, HostState currentState, HostState destState, HostState afterTransitionState, double transitionEndTime) {
		statelog.add(new HostStateAwareHistoryEntry(currentTime, currentState, destState, afterTransitionState, transitionEndTime));
	}

}
