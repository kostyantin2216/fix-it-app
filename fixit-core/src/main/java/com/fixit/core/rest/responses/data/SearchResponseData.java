package com.fixit.core.rest.responses.data;

/**
 * Created by konstantin on 3/30/2017.
 */

public class SearchResponseData {

    private boolean addressSupported;
    private String searchId;

    public boolean isAddressSupported() {
        return addressSupported;
    }

    public void setAddressSupported(boolean addressSupported) {
        this.addressSupported = addressSupported;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    @Override
    public String toString() {
        return "SearchResponseData{" +
                "addressSupported=" + addressSupported +
                ", searchId='" + searchId + '\'' +
                '}';
    }
}
