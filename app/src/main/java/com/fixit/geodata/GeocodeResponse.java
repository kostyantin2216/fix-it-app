package com.fixit.geodata;

import java.util.List;

/**
 * Created by konstantin on 3/30/2017.
 */

public class GeocodeResponse {

    private List<GeocodeResult> results;
    private String status;

    public List<GeocodeResult> getResults() {
        return results;
    }

    public void setResults(List<GeocodeResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GeocodeResponse{" +
                "results=" + results +
                ", status='" + status + '\'' +
                '}';
    }
}
