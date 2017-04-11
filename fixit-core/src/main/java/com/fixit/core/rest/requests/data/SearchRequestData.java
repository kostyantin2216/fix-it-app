package com.fixit.core.rest.requests.data;

/**
 * Created by konstantin on 3/30/2017.
 */

public class SearchRequestData {

    private int professionId;
    private String address;

    public SearchRequestData() { }

    public SearchRequestData(int professionId, String address) {
        this.professionId = professionId;
        this.address = address;
    }

    public int getProfessionId() {
        return professionId;
    }

    public void setProfessionId(int professionId) {
        this.professionId = professionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SearchRequestData{" +
                "professionId=" + professionId +
                ", address='" + address + '\'' +
                '}';
    }
}
