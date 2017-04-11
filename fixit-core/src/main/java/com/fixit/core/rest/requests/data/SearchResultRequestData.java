package com.fixit.core.rest.requests.data;

/**
 * Created by konstantin on 4/3/2017.
 */

public class SearchResultRequestData {

    private String searchId;

    public SearchResultRequestData(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    @Override
    public String toString() {
        return "SearchResultRequestData{" +
                "searchId='" + searchId + '\'' +
                '}';
    }
}
