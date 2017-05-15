package com.fixit.core.rest.requests.data;

import com.fixit.core.data.UserAccountDetails;

/**
 * Created by konstantin on 5/15/2017.
 */

public class UserRegistrationRequestData {

    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String avatarUrl;
    private String googleId;
    private String facebookId;

    public UserRegistrationRequestData() { }

    public UserRegistrationRequestData(String firstName, String lastName, String email, String telephone,
                                       String avatarUrl, String googleId, String facebookId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.avatarUrl = avatarUrl;
        this.googleId = googleId;
        this.facebookId = facebookId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public String toString() {
        return "UserRegistrationRequestData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", googleId='" + googleId + '\'' +
                ", facebookId='" + facebookId + '\'' +
                '}';
    }

}
