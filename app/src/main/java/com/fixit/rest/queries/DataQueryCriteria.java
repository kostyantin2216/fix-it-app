package com.fixit.rest.queries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 5/17/2017.
 */

public class DataQueryCriteria {

    private List<DataApiQuery> queries;

    private DataQueryCriteria() {
        queries = new ArrayList<>();
    }

    public static DataQueryCriteria create() {
        return new DataQueryCriteria();
    }

    public static DataQueryCriteria create(DataApiQuery query) {
        return new DataQueryCriteria().add(query);
    }

    public DataQueryCriteria add(DataApiQuery... queries) {
        for(DataApiQuery query : queries) {
            this.queries.add(query);
        }
        return this;
    }

    public List<DataApiQuery> queries() {
        return queries;
    }

    public DataApiQuery[] build() {
        return queries.toArray(new DataApiQuery[queries.size()]);
    }

    public void reset() {
        queries.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(DataApiQuery query : queries) {
            sb.append(query.toString()).append(" AND ");
        }
        return sb.toString();
    }

}
