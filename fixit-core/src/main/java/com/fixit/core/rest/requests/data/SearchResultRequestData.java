package com.fixit.core.rest.requests.data;

/**
 * Created by konstantin on 4/3/2017.
 */

public class SearchResultRequestData {

    private String searchKey;

    public SearchResultRequestData(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public String toString() {
        return "SearchResultRequestData{" +
                "searchKey='" + searchKey + '\'' +
                '}';
    }
}
