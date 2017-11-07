package com.fixit.data;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.fixit.utils.CommonUtils;
import com.fixit.utils.DataUtils;

import java.util.ArrayList;

/**
 * Created by Kostyantin on 4/12/2017.
 */

public class JobLocation implements Parcelable {

    public static JobLocation create(Address address) {
        JobLocation jobLocation = new JobLocation();
        jobLocation.setStreet(address.getThoroughfare());
        String streetNum = address.getSubThoroughfare();
        if(!CommonUtils.isNumber(streetNum)) {
            streetNum = address.getFeatureName();
        }
        if(CommonUtils.isNumber(streetNum)) {
            jobLocation.setStreetNum(Integer.parseInt(streetNum));
        }
        jobLocation.setCity(address.getLocality());
        jobLocation.setNeighborhood(address.getSubLocality());
        jobLocation.setProvince(address.getAdminArea());
        jobLocation.setZipCode(address.getPostalCode());
        jobLocation.setLat(address.getLatitude());
        jobLocation.setLng(address.getLongitude());
        jobLocation.setGoogleAddress(DataUtils.combineAddressLines(address));
        return jobLocation;
    }

    private String province;
    private String city;
    private String neighborhood;
    private String street;
    private String zipCode;
    private int streetNum;
    private int apartmentNum;
    private int floorNum;
    private double lat;
    private double lng;
    private String mapAreaId;
    private String comment;
    private String googleAddress;

    public JobLocation() { }

    public JobLocation(String province, String city, String neighborhood, String street, String zipCode, int streetNum,
                       int apartmentNum, int floorNum, double lat, double lng, String mapAreaId, String comment) {
        this.province = province;
        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
        this.zipCode = zipCode;
        this.streetNum = streetNum;
        this.apartmentNum = apartmentNum;
        this.floorNum = floorNum;
        this.lat = lat;
        this.lng = lng;
        this.mapAreaId = mapAreaId;
        this.comment = comment;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(int streetNum) {
        this.streetNum = streetNum;
    }

    public int getApartmentNum() {
        return apartmentNum;
    }

    public void setApartmentNum(int apartmentNum) {
        this.apartmentNum = apartmentNum;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
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

    public String getMapAreaId() {
        return mapAreaId;
    }

    public void setMapAreaId(String mapAreaId) {
        this.mapAreaId = mapAreaId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(String googleAddress) {
        this.googleAddress = googleAddress;
    }

    public String toShortAddress() {
        return streetNum + " " + street + ", " + neighborhood + ", " + city;
    }

    @Override
    public String toString() {
        return "JobLocation{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", streetNum=" + streetNum +
                ", apartmentNum=" + apartmentNum +
                ", floorNum=" + floorNum +
                ", lat=" + lat +
                ", lng=" + lng +
                ", mapAreaId='" + mapAreaId + '\'' +
                ", comment='" + comment + '\'' +
                ", googleAddress='" + googleAddress + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.neighborhood);
        dest.writeString(this.street);
        dest.writeString(this.zipCode);
        dest.writeInt(this.streetNum);
        dest.writeInt(this.apartmentNum);
        dest.writeInt(this.floorNum);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.mapAreaId);
        dest.writeString(this.comment);
        dest.writeString(this.googleAddress);
    }

    protected JobLocation(Parcel in) {
        this.province = in.readString();
        this.city = in.readString();
        this.neighborhood = in.readString();
        this.street = in.readString();
        this.zipCode = in.readString();
        this.streetNum = in.readInt();
        this.apartmentNum = in.readInt();
        this.floorNum = in.readInt();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.mapAreaId = in.readString();
        this.comment = in.readString();
        this.googleAddress = in.readString();
    }

    public static final Creator<JobLocation> CREATOR = new Creator<JobLocation>() {
        @Override
        public JobLocation createFromParcel(Parcel source) {
            return new JobLocation(source);
        }

        @Override
        public JobLocation[] newArray(int size) {
            return new JobLocation[size];
        }
    };
}
