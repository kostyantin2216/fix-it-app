package com.fixit.external.google;

import com.fixit.utils.FILog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kostyantin on 10/31/2017.
 */

public class GoogleMapWrapper {

    private final GoogleMap mMap;

    public GoogleMapWrapper(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public Marker showMarker(double latitude, double longitude, String title, String snippet, boolean showInfoWindow) {
        LatLng latLng = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet));

        FILog.i("zoom " + mMap.getCameraPosition().zoom);
        if(mMap.getCameraPosition().zoom < 10f) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        if(showInfoWindow) {
            marker.showInfoWindow();
        }

        return marker;
    }

}
