package com.fixit.core.data;

import java.util.Arrays;

/**
 * Created by konstantin on 4/2/2017.
 */

public class Tradesman {

    private String _id;
    private int professionId;
    private String contactName;
    private String companyName;
    private String email;
    private String telephone;
    private String password;
    private String logoUrl;
    private float rating;
    private MutableLatLng lastKnownLocation;
    private String[] workingAreas;
    private WorkingDay[] workingDays;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getProfessionId() {
        return professionId;
    }

    public void setProfessionId(int professionId) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String[] getWorkingAreas() {
        return workingAreas;
    }

    public void setWorkingAreas(String[] workingAreas) {
        this.workingAreas = workingAreas;
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
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", rating=" + rating +
                ", lastKnownLocation=" + lastKnownLocation +
                ", workingAreas=" + Arrays.toString(workingAreas) +
                ", workingDays=" + Arrays.toString(workingDays) +
                '}';
    }


}
