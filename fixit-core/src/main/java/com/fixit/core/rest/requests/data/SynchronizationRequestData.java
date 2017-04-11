package com.fixit.core.rest.requests.data;

import com.fixit.core.synchronization.SynchronizationAction;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kostyantin on 3/27/2017.
 */

public class SynchronizationRequestData {

    private Date firstSynchronization;
    private Map<String, Set<SynchronizationAction>> synchronizationHistory;

    public SynchronizationRequestData() { }

    public SynchronizationRequestData(Date firstSynchronization, Map<String, Set<SynchronizationAction>> synchronizationHistory) {
        this.firstSynchronization = firstSynchronization;
        this.synchronizationHistory = synchronizationHistory;
    }

    public Date setFirstSynchronization() {
        return firstSynchronization;
    }

    public void setFirstSynchronization(Date firstSynchronization) {
        this.firstSynchronization = firstSynchronization;
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
