package com.fixit.core.rest.requests.data;

import com.fixit.core.data.MutableLatLng;

/**
 * Created by konstantin on 3/30/2017.
 */

public class SearchRequestData {

    private int professionId;
    private MutableLatLng latLng;

    public SearchRequestData() { }

    public SearchRequestData(int professionId, MutableLatLng latLng) {
        this.professionId = professionId;
        this.latLng = latLng;
    }

    public int getProfessionId() {
        return professionId;
    }

    public void setProfessionId(int professionId) {
        this.professionId = professionId;
    }

    public MutableLatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(MutableLatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "SearchRequestData{" +
                "professionId=" + professionId +
                ", latLng=" + latLng +
                '}';
    }
}
