package org.cloudbus.cloudsim;

/**
 * Created by ponaszki on 2017-12-29.
 */
public class VmMigrationHistoryEntry {
    double startMigrationTime;
    int sourceHost;
    int destinationHost;
    int vmId;
    double migrationTime;

    public VmMigrationHistoryEntry(double startMigrationTime, int sourceHost, int destinationHost, int vmId, double migrationTime) {
        this.startMigrationTime = startMigrationTime;
        this.sourceHost = sourceHost;
        this.destinationHost = destinationHost;
        this.vmId = vmId;
        this.migrationTime = migrationTime;
    }

    public double getStartMigrationTime() {
        return startMigrationTime;
    }

    public void setStartMigrationTime(long startMigrationTime) {
        this.startMigrationTime = startMigrationTime;
    }

    public int getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(int sourceHost) {
        this.sourceHost = sourceHost;
    }

    public int getDestinationHost() {
        return destinationHost;
    }

    public void setDestinationHost(int destinationHost) {
        this.destinationHost = destinationHost;
    }

    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public double getMigrationTime() {
        return migrationTime;
    }

    public void setMigrationTime(double migrationTime) {
        this.migrationTime = migrationTime;
    }
}
