package com.fixit.geo.data;

import com.fixit.data.MutableLatLng;
import com.fixit.data.MutableLatLngBounds;

/**
 * Created by konstantin on 3/30/2017.
 */

public class GeocodeGeometry {

    private MutableLatLngBounds bounds;
    private MutableLatLng location;
    private String locationType;
    private MutableLatLngBounds viewport;

    public MutableLatLngBounds getBounds() {
        return bounds;
    }

    public void setBounds(MutableLatLngBounds bounds) {
        this.bounds = bounds;
    }

    public MutableLatLng getLocation() {
        return location;
    }

    public void setLocation(MutableLatLng location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public MutableLatLngBounds getViewport() {
        return viewport;
    }

    public void setViewport(MutableLatLngBounds viewport) {
        this.viewport = viewport;
    }

    @Override
    public String toString() {
        return "GeocodeGeometry{" +
                "bounds=" + bounds +
                ", location=" + location +
                ", locationType='" + locationType + '\'' +
                ", viewport=" + viewport +
                '}';
    }
}
