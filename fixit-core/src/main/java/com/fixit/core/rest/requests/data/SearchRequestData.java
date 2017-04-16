package com.fixit.core.rest.requests.data;

import com.fixit.core.data.MutableLatLng;

/**
 * Created by konstantin on 3/30/2017.
 */

public class SearchRequestData {

    private int professionId;
    private MutableLatLng location;

    public SearchRequestData() { }

    public SearchRequestData(int professionId, MutableLatLng location) {
        this.professionId = professionId;
        this.location = location;
    }

    public int getProfessionId() {
        return professionId;
    }

    public void setProfessionId(int professionId) {
        this.professionId = professionId;
    }

    public MutableLatLng getLocation() {
        return location;
    }

    public void setLocation(MutableLatLng location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "SearchRequestData{" +
                "professionId=" + professionId +
                ", location=" + location +
                '}';
    }
}
