package com.fixit.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fixit.app.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by konstantin on 11/1/2017.
 */

public class CommonMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final LayoutInflater mLayoutInflater;

    public CommonMarkerInfoWindowAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View v = mLayoutInflater.inflate(R.layout.layout_common_marker_info_window, null);

        TextView tvProfession = (TextView) v.findViewById(R.id.tv_profession_at);
        TextView tvAddress = (TextView) v.findViewById(R.id.tv_address);

        tvProfession.setText(v.getResources().getString(R.string.i_need_profession_at, marker.getTitle()));
        tvAddress.setText(marker.getSnippet());

        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


}

