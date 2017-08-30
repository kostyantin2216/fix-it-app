package com.fixit.rest.requests.data;

import com.fixit.data.MutableLatLng;

/**
 * Created by konstantin on 3/30/2017.
 */

public class SearchRequestData {

    private long professionId;
    private MutableLatLng location;

    public SearchRequestData() { }

    public SearchRequestData(long professionId, MutableLatLng location) {
        this.professionId = professionId;
        this.location = location;
    }

    public long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(long professionId) {
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
