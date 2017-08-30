package com.fixit.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by konstantin on 4/2/2017.
 */
public class Tradesman implements Parcelable {

    private String _id;
    private long professionId;
    private String contactName;
    private String companyName;
    private String telephone;
    private String logoUrl;
    private String featureImageUrl;
    private float rating;
    private MutableLatLng lastKnownLocation;
    private WorkingDay[] workingDays;

    public Tradesman() { }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(long professionId) {
        this.professionId = professionId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFeatureImageUrl() {
        return featureImageUrl;
    }

    public void setFeatureImageUrl(String featureImageUrl) {
        this.featureImageUrl = featureImageUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public MutableLatLng getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(MutableLatLng lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public WorkingDay[] getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(WorkingDay[] workingDays) {
        this.workingDays = workingDays;
    }

    @Override
    public String toString() {
        return "Tradesman{" +
                "_id='" + _id + '\'' +
                ", professionId=" + professionId +
                ", contactName='" + contactName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", featureImageUrl='" + featureImageUrl + '\'' +
                ", rating=" + rating +
                ", lastKnownLocation=" + lastKnownLocation +
                ", workingDays=" + Arrays.toString(workingDays) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeLong(this.professionId);
        dest.writeString(this.contactName);
        dest.writeString(this.companyName);
        dest.writeString(this.logoUrl);
        dest.writeFloat(this.rating);
        dest.writeParcelable(this.lastKnownLocation, flags);
        dest.writeTypedArray(this.workingDays, flags);
    }

    protected Tradesman(Parcel in) {
        this._id = in.readString();
        this.professionId = in.readLong();
        this.contactName = in.readString();
        this.companyName = in.readString();
        this.logoUrl = in.readString();
        this.rating = in.readFloat();
        this.lastKnownLocation = in.readParcelable(MutableLatLng.class.getClassLoader());
        this.workingDays = in.createTypedArray(WorkingDay.CREATOR);
    }

    public static final Creator<Tradesman> CREATOR = new Creator<Tradesman>() {
        @Override
        public Tradesman createFromParcel(Parcel source) {
            return new Tradesman(source);
        }

        @Override
        public Tradesman[] newArray(int size) {
            return new Tradesman[size];
        }
    };
}