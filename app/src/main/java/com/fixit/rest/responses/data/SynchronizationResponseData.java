package com.fixit.rest.responses.data;

import com.fixit.synchronization.SynchronizationResult;

import java.util.Iterator;
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

    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        int resultCount = synchronizationResults.size();
        if(resultCount > 0) {
            for(int i = 0; i < resultCount; i++) {
                SynchronizationResult result = synchronizationResults.get(i);
                SynchronizationResult.Result[] results = result.getResults();
                sb.append(result.getName()).append("[").append(results != null ? results.length : 0).append("]");
                if (!result.isSupported()) {
                    sb.append("(UNSUPPORTED)");
                }
                if(i - 1 < resultCount) {
                    sb.append("; ");
                }
            }
        } else {
            sb.append("NA");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "SynchronizationResponseData{" +
                "synchronizationResults=" + synchronizationResults +
                '}';
    }

}
