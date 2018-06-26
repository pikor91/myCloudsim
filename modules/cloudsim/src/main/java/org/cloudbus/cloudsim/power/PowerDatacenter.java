/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;

import java.util.*;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.predicates.PredicateType;

/**
 * PowerDatacenter is a class that enables simulation of power-aware data centers.
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
public class PowerDatacenter extends Datacenter {

	/** The datacenter consumed power. */
	private double power;

	/** Indicates if migrations are disabled or not. */
	private boolean disableMigrations;

	private boolean enableSwithcingHostsState;

	/** The last time submitted cloudlets were processed. */
	private double cloudletSubmitted;

	/** The VM migration count. */
	private int migrationCount;


	/** added by ponaszki */
	private Map<Double, Double> timeFramesPowers = new TreeMap<>();

	/**
	 * Instantiates a new PowerDatacenter.
	 * 
	 * @param name the datacenter name
	 * @param characteristics the datacenter characteristics
	 * @param schedulingInterval the scheduling interval
	 * @param vmAllocationPolicy the vm provisioner
	 * @param storageList the storage list
	 * @throws Exception the exception
	 */
	public PowerDatacenter(
			String name,
			DatacenterCharacteristics characteristics,
			VmAllocationPolicy vmAllocationPolicy,
			List<Storage> storageList,
			double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);

		setPower(0.0);
		setDisableMigrations(false);
		setEnableSwithcingHostsState(Consts.ENABLE_HS);
		setCloudletSubmitted(-1);
		setMigrationCount(0);
	}

	@Override
	protected void updateCloudletProcessing() {
		if (getCloudletSubmitted() == -1 || getCloudletSubmitted() == CloudSim.clock()) {
			CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
			schedule(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
			return;
		}
		double currentTime = CloudSim.clock();

		// if some time passed since last processing
		if (currentTime > getLastProcessTime()) {
			System.out.print(currentTime + " ");

			double minTime = updateCloudetProcessingWithoutSchedulingFutureEventsForce();
			//todo number of hosts in transition.
			if(isEnableSwithcingHostsState()){
				List <? extends Host> activeHosts = getActiveHosts(getHostList());
				List <? extends Host> inactiveHosts = getInactiveHosts(getHostList());
				List <? extends Host> betweenHosts = getBetweenHosts(getHostList());
				for(Host host : inactiveHosts){
					PowerHost ph = (PowerHost) host;
					if(!ph.getVmList().isEmpty() || !ph.getVmsMigratingIn().isEmpty() || ph.getUtilizationOfCpu()>0){
						throw new IllegalStateException("inactive host with vm list not empty");
					}
				}

				if (!betweenHosts.isEmpty()){

					for(Host host : betweenHosts){
						PowerHost ph = (PowerHost) host;
						if(!ph.getVmList().isEmpty() || !ph.getVmsMigratingIn().isEmpty() || ph.getUtilizationOfCpu()>0){
							throw new IllegalStateException("transition host with vm list not empty");
						}
					}
					throw new IllegalStateException("There are hosts during transition.");
				}
			}

			if (!isDisableMigrations()) {
				List<Map<String, Object>> migrationMap = getVmAllocationPolicy().optimizeAllocation(
						getVmList());

				if (migrationMap != null) {
					for (Map<String, Object> migrate : migrationMap) {
						Vm vm = (Vm) migrate.get("vm");
						PowerHostStateAware targetHost = (PowerHostStateAware) migrate.get("host");
						PowerHostStateAware oldHost = (PowerHostStateAware) vm.getHost();

						if (oldHost == null) {
							Log.formatLine(
									"%.2f: Migration of VM #%d to Host #%d is started",
									currentTime,
									vm.getId(),
									targetHost.getId());
						} else if(oldHost!=null && targetHost == null){
							Log.formatLine(
									"%.2f: Migration of VM #%d from host #%d is not possible. No target host found.",
									currentTime,
									vm.getId(),
									oldHost.getId());
						} else {
							Log.formatLine(
									"%.2f: Migration of VM #%d from Host #%d to Host #%d is started ",
									currentTime,
									vm.getId(),
									oldHost.getId(),
									targetHost.getId());
						}

						if (!isEnableSwithcingHostsState()){
							//if switching off/on hosts is enabled
							targetHost.addMigratingInVm(vm);
							incrementMigrationCount();
							/** VM migration delay = RAM / bandwidth **/
							// we use BW / 2 to model BW available for migration purposes, the other
							// half of BW is for VM communication
							// around 16 seconds for 1024 MB using 1 Gbit/s network
							double delay = vm.getRam() / ((double) targetHost.getBw() / (2 * 8000));
//							double delay = 180;

							send(
									getId(),
									delay,
									CloudSimTags.VM_MIGRATE,
									migrate);
						}else{
							//if switching off/on hosts is enabled
							//if source host is underutilised send switchOff
							if(oldHost != null && oldHost.isUnderUtilized()) {
								Log.formatLine("%.2f: Source host #%d is marked for switch Off.", currentTime, oldHost.getId());
							}

							if(targetHost != null) {
								targetHost.addMigratingInVm(vm);
								incrementMigrationCount();
								if(((PowerHostStateAware)targetHost).isActive()){
									double delay = countDelay(targetHost, vm);
									send(
											getId(),
											delay,
											CloudSimTags.VM_MIGRATE,
											migrate);
								}else{
									sendSwitchOnHost(targetHost, vm, 0);
								}


//								Log.printConcatLine("%d: target host #%d is turned off. Sending switching on event. ", currentTime, targetHost.getId());
//								double delay = (vm.getRam() / ((double) targetHost.getBw() / (2 * 8000)));
//								PowerModelSpecPower powerModel = (PowerModelSpecPower) (targetHost.getPowerModel());
//								int transitionTime = powerModel.getTransitionTime(HostState.INACTIVE, HostState.ACTIVE);
//								delay += transitionTime;

							}
						}

					}
				}
			}

			// schedules an event to the next time
			if (minTime != Double.MAX_VALUE) {
				CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
				send(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
			}

			setLastProcessTime(currentTime);
		}
	}

	public double countDelay(PowerHost newHost, Vm vm) {
		if(newHost == null){
			throw new IllegalStateException("Cannot count delay time for migration, destination host is null");
		}
		PowerHostStateAware nh = (PowerHostStateAware) newHost;
		double transferTime = vm.getRam() / ((double) nh.getBw() / (2 * 8000));
		double delay = -1;
		if(Consts.ENABLE_HS) {
			if (nh.isInactive() && !nh.isDuringTransition()) {
				//jeżeli jest wyłączony
				double switchOnTime = nh.getPowerModel().getTransitionTime(HostState.INACTIVE, HostState.ACTIVE);
				delay = switchOnTime + transferTime;
			} else if (nh.isInactive() && nh.isDuringTransition()) {
				//jeżeli już zaczal się włączać to trzeba poczekać aż się włączy i zmigrować tam VM
				double endTransitionDuration = nh.getTransitionEndTime() - CloudSim.clock();
				delay = endTransitionDuration + transferTime;
			} else if (nh.isActive() && !nh.isDuringTransition()) {
				//jezeli jest wlaczony
				delay = transferTime;
			} else if (nh.isActive() && nh.isDuringTransition()) {
				//jeżeli jest wlaczony i w trakciewylaczania
				double endTransitionDuration = nh.getTransitionEndTime() - CloudSim.clock();
				double turnOnTime = nh.getPowerModel().getTransitionTime(HostState.INACTIVE, HostState.ACTIVE);
				delay = endTransitionDuration + turnOnTime + transferTime;
			} else {
				Log.printConcatLine("UnexpectedState during turning on host #%d", nh.getId());
				System.exit(0);
			}
		}else{
			delay = transferTime;
		}
		return delay;
	}
	private List<? extends Host> getBetweenHosts(List<Host> hostList) {
		List<Host> betweenHosts = new LinkedList<>();
		for(Host h : hostList){
			PowerHostStateAware ph = (PowerHostStateAware) h;
			if(ph.isDuringTransition()){
				betweenHosts.add(ph);
			}
		}
		return betweenHosts;
	}

	private List<? extends Host> getInactiveHosts(List<Host> hostList) {
		List<Host> inactiveHosts = new LinkedList<>();
		for(Host h : hostList){
			PowerHostStateAware ph = (PowerHostStateAware) h;
			if(HostState.INACTIVE.equals(ph.getCurrentState()) && !ph.isDuringTransition()){
				inactiveHosts.add(ph);
			}
		}
		return inactiveHosts;
	}

	private List<? extends Host> getActiveHosts(List<Host> hostList) {
		List<Host> activeHosts = new LinkedList<>();
		for(Host h : hostList){
			PowerHostStateAware ph = (PowerHostStateAware) h;
			if(HostState.ACTIVE.equals(ph.getCurrentState()) && !ph.isDuringTransition()){
				activeHosts.add(ph);
			}
		}
		return activeHosts;
	}

	private void sendSwitchOffHost(PowerHostStateAware oldHost, double delay) {
		Map<String, Object> args = new HashMap<>();
		args.put(Consts.HOST, oldHost);
		args.put(Consts.START_STATE, HostState.ACTIVE);
		args.put(Consts.END_STATE, HostState.INACTIVE);
		args.put(Consts.VM, null);
		send(getId(), delay, CloudSimTags.HOST_CHANGE_STATE_START, args );
	}

	private void sendSwitchOnHost(PowerHostStateAware oldHost, Vm migratingVm, double delay) {
		Map<String, Object> args = new HashMap<>();
		args.put(Consts.HOST, oldHost);
		args.put(Consts.START_STATE, HostState.INACTIVE);
		args.put(Consts.END_STATE, HostState.ACTIVE);
		args.put(Consts.VM, migratingVm);
		send(getId(), delay, CloudSimTags.HOST_CHANGE_STATE_START, args );
	}

	private Host getStandbyHostForVm(Vm vm) {
		double minimalUtilization = Double.MAX_VALUE;
		Host minimalHost = null;
		for(Host h : this.getStandbyHostList()){
			PowerHostStateAware host = (PowerHostStateAware) h;
			if(host.getUtilizationOfCpu() < minimalUtilization && host.isSuitableForVm(vm)){
				minimalUtilization = host.getUtilizationOfCpu();
				minimalHost = host;
			}
		}
		return minimalHost;
	}

	private Host getPoweredOffHostForVm(Vm vm) {
		for(Host h : this.getStandbyHostList()){
			PowerHostStateAware host = (PowerHostStateAware) h;
			if(host.isInactive() && host.isSuitableForVm(vm)){
				return h;
			}
		}
		return null;
	}



	/**
	 * Update cloudet processing without scheduling future events.
	 * 
	 * @return the double
         * @see #updateCloudetProcessingWithoutSchedulingFutureEventsForce() 
         * @todo There is an inconsistence in the return value of this
         * method with return value of similar methods
         * such as {@link #updateCloudetProcessingWithoutSchedulingFutureEventsForce()},
         * that returns {@link Double#MAX_VALUE} by default.
         * The current method returns 0 by default.
	 */
	protected double updateCloudetProcessingWithoutSchedulingFutureEvents() {
		if (CloudSim.clock() > getLastProcessTime()) {
			return updateCloudetProcessingWithoutSchedulingFutureEventsForce();
		}
		return 0;
	}

	/**
	 * Update cloudet processing without scheduling future events.
	 * 
	 * @return expected time of completion of the next cloudlet in all VMs of all hosts or
	 *         {@link Double#MAX_VALUE} if there is no future events expected in this host
	 */
	protected double updateCloudetProcessingWithoutSchedulingFutureEventsForce() {
		double currentTime = CloudSim.clock();
		double minTime = Double.MAX_VALUE;
		double timeDiff = currentTime - getLastProcessTime();
		double timeFrameDatacenterEnergy = 0.0;

		Log.printLine("\n\n--------------------------------------------------------------\n\n");
		Log.formatLine("New resource usage for the time frame starting at %.2f:", currentTime);

		for (PowerHost host : this.<PowerHost> getHostList()) {
			Log.printLine();

			double time = host.updateVmsProcessing(currentTime); // inform VMs to update processing
			if (time < minTime) {
				minTime = time;
			}

			Log.formatLine(
					"%.2f: [Host #%d] utilization is %.2f%%",
					currentTime,
					host.getId(),
					host.getUtilizationOfCpu() * 100);
		}

		if (timeDiff > 0) {
			Log.formatLine(
					"\nEnergy consumption for the last time frame from %.2f to %.2f:",
					getLastProcessTime(),
					currentTime);

			for (PowerHost host : this.<PowerHost> getHostList()) {
				double previousUtilizationOfCpu = host.getPreviousUtilizationOfCpu();
				double utilizationOfCpu = host.getUtilizationOfCpu();
				double timeFrameHostEnergy = host.getEnergyLinearInterpolation(
						previousUtilizationOfCpu,
						utilizationOfCpu,
						timeDiff);
				timeFrameDatacenterEnergy += timeFrameHostEnergy;


				Log.printLine();
				Log.formatLine(
						"%.2f: [Host #%d] utilization at %.2f was %.2f%%, now is %.2f%%",
						currentTime,
						host.getId(),
						getLastProcessTime(),
						previousUtilizationOfCpu * 100,
						utilizationOfCpu * 100);
				Log.formatLine(
						"%.2f: [Host #%d] energy is %.2f W*sec",
						currentTime,
						host.getId(),
						timeFrameHostEnergy);
			}

			Log.formatLine(
					"\n%.2f: Data center's energy is %.2f W*sec\n",
					currentTime,
					timeFrameDatacenterEnergy);
		}

		setPower(getPower() + timeFrameDatacenterEnergy);
		addTimeFramePower(currentTime, timeFrameDatacenterEnergy);
		checkCloudletCompletion();

		/** Remove completed VMs **/
		for (PowerHost host : this.<PowerHost> getHostList()) {
			for (Vm vm : host.getCompletedVms()) {
				getVmAllocationPolicy().deallocateHostForVm(vm);
				getVmList().remove(vm);
				Log.printLine("VM #" + vm.getId() + " has been deallocated from host #" + host.getId());
			}
		}

		Log.printLine();

		setLastProcessTime(currentTime);
		return minTime;
	}

	private void addTimeFramePower(double currentTime, double timeFrameDatacenterEnergy) {
		Double powerInThisFrame = timeFramesPowers.get(new Double(currentTime));
		if(powerInThisFrame == null){
			timeFramesPowers.put(new Double(currentTime), new Double(timeFrameDatacenterEnergy));
		}else {
			timeFramesPowers.put(new Double(currentTime), new Double(powerInThisFrame + timeFrameDatacenterEnergy));
		}
	}

	@Override
	protected boolean processVmMigrate(SimEvent ev, boolean ack) {
		updateCloudetProcessingWithoutSchedulingFutureEvents();

		Object tmp = ev.getData();
		Map<String, Object> migrate = (HashMap<String, Object>) tmp;

		Vm vm = (Vm) migrate.get("vm");
		Host sourceHost = vm.getHost();
		boolean allocationSuccessfull = super.processVmMigrate(ev, ack);

		if(allocationSuccessfull && isEnableSwithcingHostsState()){
			sendSwitchOffIfVmIsEmpty(sourceHost, ev);
		}
		SimEvent event = CloudSim.findFirstDeferred(getId(), new PredicateType(CloudSimTags.VM_MIGRATE));
		if (event == null || event.eventTime() > CloudSim.clock()) {
			updateCloudetProcessingWithoutSchedulingFutureEventsForce();
		}
		return allocationSuccessfull;
	}

	private void sendSwitchOffIfVmIsEmpty(Host sourceHost, SimEvent ev) {
		Object tmp = ev.getData();
		Map<String, Object> migrate = (HashMap<String, Object>) tmp;
		Vm vm = (Vm) migrate.get("vm");
		Host host = (Host) migrate.get("host");

		int sourceVmListSize = sourceHost.getVmList().size();
		int sourceMigratingInListSize = sourceHost.getVmsMigratingIn().size();

		if( sourceVmListSize == 0 && sourceMigratingInListSize == 0 &&  isEnableSwithcingHostsState()){
			Log.printConcatLine("#%d successfull migration of last VM #%d from host #%d to host #%d", CloudSim.clock(), vm.getId(), sourceHost.getId(), host.getId());
			Map<String, Object> args = getArgs(sourceHost, HostState.ACTIVE, HostState.INACTIVE, null);
			send(getId(), 0, CloudSimTags.HOST_CHANGE_STATE_START, args);
		}

	}

	@Override
	protected void processCloudletSubmit(SimEvent ev, boolean ack) {
		super.processCloudletSubmit(ev, ack);
		setCloudletSubmitted(CloudSim.clock());
	}

	private Map<String, Object> getArgs(Host sourceHost, HostState startState, HostState endState, Vm vm) {
		Map <String, Object> args = new HashMap<>();
		args.put(Consts.HOST, sourceHost);
		args.put(Consts.START_STATE, startState);
		args.put(Consts.END_STATE, endState);
		args.put(Consts.VM, vm);
		return args;
	}

	/**
	 * Gets the power.
	 * 
	 * @return the power
	 */
	public double getPower() {
		return power;
	}

	/**
	 * Sets the power.
	 * 
	 * @param power the new power
	 */
	protected void setPower(double power) {
		this.power = power;
	}

	/**
	 * Checks if PowerDatacenter is in migration.
	 * 
	 * @return true, if PowerDatacenter is in migration; false otherwise
	 */
	protected boolean isInMigration() {
		boolean result = false;
		for (Vm vm : getVmList()) {
			if (vm.isInMigration()) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Checks if migrations are disabled.
	 * 
	 * @return true, if  migrations are disable; false otherwise
	 */
	public boolean isDisableMigrations() {
		return disableMigrations;
	}

	/**
	 * Disable or enable migrations.
	 * 
	 * @param disableMigrations true to disable migrations; false to enable
	 */
	public void setDisableMigrations(boolean disableMigrations) {
		this.disableMigrations = disableMigrations;
	}

	/**
	 * Checks if is cloudlet submited.
	 * 
	 * @return true, if is cloudlet submited
	 */
	protected double getCloudletSubmitted() {
		return cloudletSubmitted;
	}

	/**
	 * Sets the cloudlet submitted.
	 * 
	 * @param cloudletSubmitted the new cloudlet submited
	 */
	protected void setCloudletSubmitted(double cloudletSubmitted) {
		this.cloudletSubmitted = cloudletSubmitted;
	}

	/**
	 * Gets the migration count.
	 * 
	 * @return the migration count
	 */
	public int getMigrationCount() {
		return migrationCount;
	}

	/**
	 * Sets the migration count.
	 * 
	 * @param migrationCount the new migration count
	 */
	protected void setMigrationCount(int migrationCount) {
		this.migrationCount = migrationCount;
	}

	/**
	 * Increment migration count.
	 */
	protected void incrementMigrationCount() {
		setMigrationCount(getMigrationCount() + 1);
	}

	/**
	 * added by ponaszki
	 * @return
	 */
	public Map<Double, Double> getTimeFramesPowers() {
		return timeFramesPowers;
	}



	public boolean isEnableSwithcingHostsState() {
		return enableSwithcingHostsState;
	}

	public void setEnableSwithcingHostsState(boolean enableSwithcingHostsState) {
		this.enableSwithcingHostsState = enableSwithcingHostsState;
	}

}
