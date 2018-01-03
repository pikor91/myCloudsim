package org.cloudbus.cloudsim;

/**
 * Created by ponaszki on 2017-12-29.
 */
public class VmMigrationHistoryEntry {
    double migrationTime;
    int sourceHost;
    int destinationHost;
    int vmId;

    public VmMigrationHistoryEntry(double migrationTime, int sourceHost, int destinationHost, int vmId) {
        this.migrationTime = migrationTime;
        this.sourceHost = sourceHost;
        this.destinationHost = destinationHost;
        this.vmId = vmId;
    }

    public double getMigrationTime() {
        return migrationTime;
    }

    public void setMigrationTime(long migrationTime) {
        this.migrationTime = migrationTime;
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
}
