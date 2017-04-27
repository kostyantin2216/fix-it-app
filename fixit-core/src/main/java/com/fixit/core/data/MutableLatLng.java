package com.fixit.core.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by konstantin on 4/2/2017.
 */

public class MutableLatLng implements Parcelable {

    private double lat;
    private double lng;

    public MutableLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "MutableLatLng{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    protected MutableLatLng(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Creator<MutableLatLng> CREATOR = new Creator<MutableLatLng>() {
        @Override
        public MutableLatLng createFromParcel(Parcel source) {
            return new MutableLatLng(source);
        }

        @Override
        public MutableLatLng[] newArray(int size) {
            return new MutableLatLng[size];
        }
    };
}
