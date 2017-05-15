package com.fixit.core.rest.responses.data;

/**
 * Created by konstantin on 5/15/2017.
 */

public class UserRegistrationResponseData {

    private boolean existingEmail;
    private String userId;

    public boolean isExistingEmail() {
        return existingEmail;
    }

    public void setExistingEmail(boolean existingEmail) {
        this.existingEmail = existingEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserRegistrationResponseData{" +
                "existingEmail=" + existingEmail +
                ", userId='" + userId + '\'' +
                '}';
    }
}
