package com.fixit.core.rest.queries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 5/17/2017.
 */

public class DataQueryRequest {
    private List<DataApiQuery> queries;

    public DataQueryRequest() { }

    public DataQueryRequest(DataApiQuery... queries) {
        this.queries = new ArrayList<>();
        for(DataApiQuery query : queries) {
            this.queries.add(query);
        }
    }

    public List<DataApiQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<DataApiQuery> queries) {
        this.queries = queries;
    }
}
