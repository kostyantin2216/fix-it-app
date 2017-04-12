package com.fixit.core.rest.responses.data;

/**
 * Created by konstantin on 3/30/2017.
 */

public class SearchResponseData {

    private String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public String toString() {
        return "SearchResponseData [searchKey=" + searchKey + "]";
    }
}
