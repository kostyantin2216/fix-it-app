package com.fixit.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Kostyantin on 10/29/2017.
 */

public class BindingMapFragment extends SupportMapFragment {

    public BindingMapFragment() {
        super();
    }

    public static BindingMapFragment newInstance() {
        return new BindingMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        View v = super.onCreateView(arg0, arg1, arg2);
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof OnMapReadyCallback) {
            getMapAsync((OnMapReadyCallback) fragment);
        }
        return v;
    }
}
