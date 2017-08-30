package com.fixit.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by konstantin on 5/15/2017.
 */

public class UserAccountDetails implements Parcelable {

    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String avatarUrl;
    private String googleId;
    private String facebookId;

    public UserAccountDetails() { }

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
        return "UserAccountDetails{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", googleId='" + googleId + '\'' +
                ", facebookId='" + facebookId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.telephone);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.googleId);
        dest.writeString(this.facebookId);
    }

    protected UserAccountDetails(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.telephone = in.readString();
        this.avatarUrl = in.readString();
        this.googleId = in.readString();
        this.facebookId = in.readString();
    }

    public static final Creator<UserAccountDetails> CREATOR = new Creator<UserAccountDetails>() {
        @Override
        public UserAccountDetails createFromParcel(Parcel source) {
            return new UserAccountDetails(source);
        }

        @Override
        public UserAccountDetails[] newArray(int size) {
            return new UserAccountDetails[size];
        }
    };
}
