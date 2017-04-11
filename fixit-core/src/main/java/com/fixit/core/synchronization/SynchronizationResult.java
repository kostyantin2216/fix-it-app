package com.fixit.core.synchronization;

import com.fixit.core.data.DataModelObject;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Kostyantin on 3/27/2017.
 */

public class SynchronizationResult<DMO extends DataModelObject> {

    private String name;
    private Result<DMO>[] results;
    private boolean isSupported = false;

    public SynchronizationResult() { }

    public SynchronizationResult(String name, Result<DMO>[] results) {
        this.name = name;
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Result<DMO>[] getResults() {
        return results;
    }

    public void setResults(Result<DMO>[] results) {
        this.results = results;
    }

    public boolean isSupported() {
        return isSupported;
    }

    public void setSupported(boolean supported) {
        isSupported = supported;
    }

    @Override
    public String toString() {
        return "SynchronizationResult{" +
                "name='" + name + '\'' +
                ", results=" + Arrays.toString(results) +
                ", isSupported=" + isSupported +
                '}';
    }

    public static class Result<DMO extends DataModelObject> {
        private SynchronizationAction action;
        private List<DMO> data;
        private Set<Long> ids;

        public SynchronizationAction getAction() {
            return action;
        }

        public void setAction(SynchronizationAction action) {
            this.action = action;
        }

        public List<DMO> getData() {
            return data;
        }

        public void setData(List<DMO> data) {
            this.data = data;
        }

        public Set<Long> getIds() {
            return ids;
        }

        public void setIds(Set<Long> ids) {
            this.ids = ids;
        }

        @Override
        public String toString() {
            return "ResultData{" +
                    "action=" + action +
                    ", results=" + data +
                    ", ids=" + ids +
                    '}';
        }
    }
}
