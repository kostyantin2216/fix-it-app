package com.fixit.app.ifs.geodata;

import com.fixit.core.data.MutableLatLng;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by konstantin on 3/30/2017.
 */

public class GeocodeGeometry {

    private LatLngBounds bounds;
    private LatLng location;
    private String locationType;
    private LatLngBounds viewport;

    public LatLngBounds getBounds() {
        return bounds;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public LatLngBounds getViewport() {
        return viewport;
    }

    public void setViewport(LatLngBounds viewport) {
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
