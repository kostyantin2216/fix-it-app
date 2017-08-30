package com.fixit.rest.responses.data;

import com.fixit.synchronization.SynchronizationResult;

import java.util.List;

/**
 * Created by Kostyantin on 3/27/2017.
 */

public class SynchronizationResponseData {

    private List<SynchronizationResult> synchronizationResults;

    public SynchronizationResponseData() { }

    public SynchronizationResponseData(List<SynchronizationResult> synchronizationResults) {
        this.synchronizationResults = synchronizationResults;
    }

    public List<SynchronizationResult> getSynchronizationResults() {
        return synchronizationResults;
    }

    public void setSynchronizationResults(List<SynchronizationResult> synchronizationResults) {
        this.synchronizationResults = synchronizationResults;
    }

    @Override
    public String toString() {
        return "SynchronizationResponseData{" +
                "synchronizationResults=" + synchronizationResults +
                '}';
    }

}
