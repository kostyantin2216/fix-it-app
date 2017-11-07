package com.fixit.external.google;

import android.content.Context;
import android.graphics.Color;

import com.fixit.app.R;
import com.fixit.utils.FILog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kostyantin on 10/31/2017.
 */

public class GoogleMapWrapper {

    private final static float DEFAULT_ZOOM = 13f;

    private final GoogleMap mMap;
    private final BitmapDescriptor mMarkerIconDescriptor;

    private Padding mPadding;

    public GoogleMapWrapper(GoogleMap googleMap) {
        mMap = googleMap;
        mPadding = new Padding(0, 0, 0, 0);
        mPadding.update(mMap);

        mMarkerIconDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void moveTo(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    public Marker showMarker(double latitude, double longitude, String title, String snippet, boolean showInfoWindow) {
        LatLng latLng = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(mMarkerIconDescriptor)
        );

        FILog.i("zoom " + mMap.getCameraPosition().zoom);
        if(mMap.getCameraPosition().zoom < 13f) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        if(showInfoWindow) {
            marker.showInfoWindow();
        }

        return marker;
    }

    public void setTopPadding(int padding) {
        mPadding = mPadding.setTop(padding);
        mPadding.update(mMap);
    }

    public void setBottomPadding(int padding) {
        mPadding = mPadding.setBottom(padding);
        mPadding.update(mMap);
    }

    private static class Padding {
        final int left;
        final int top;
        final int right;
        final int bottom;

        Padding(int l, int t, int r, int b) {
            left = l;
            top = t;
            right = r;
            bottom = b;
        }

        Padding setTop(int padding) {
            return new Padding(left, padding, right, bottom);
        }

        Padding setBottom(int padding) {
            return new Padding(left, top, right, padding);
        }

        void update(GoogleMap map) {
            map.setPadding(left, top, right, bottom);
        }
    }

}
