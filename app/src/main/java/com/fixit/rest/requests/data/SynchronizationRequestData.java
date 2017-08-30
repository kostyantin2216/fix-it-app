package com.fixit.rest.requests.data;

import com.fixit.synchronization.SynchronizationAction;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kostyantin on 3/27/2017.
 */

public class SynchronizationRequestData {

    private Date lastSynchronization;
    private Map<String, Set<SynchronizationAction>> synchronizationHistory;

    public SynchronizationRequestData() { }

    public SynchronizationRequestData(Date lastSynchronization, Map<String, Set<SynchronizationAction>> synchronizationHistory) {
        this.lastSynchronization = lastSynchronization;
        this.synchronizationHistory = synchronizationHistory;
    }

    public Date setFirstSynchronization() {
        return lastSynchronization;
    }

    public void setLastSynchronization(Date lastSynchronization) {
        this.lastSynchronization = lastSynchronization;
    }

    public Map<String, Set<SynchronizationAction>> getSynchronizationHistory() {
        return synchronizationHistory;
    }

    public void setSynchronizationHistory(Map<String, Set<SynchronizationAction>> synchronizationHistory) {
        this.synchronizationHistory = synchronizationHistory;
    }

    @Override
    public String toString() {
        return "SynchronizationRequestData{" +
                "synchronizationHistory=" + synchronizationHistory +
                '}';
    }
}
