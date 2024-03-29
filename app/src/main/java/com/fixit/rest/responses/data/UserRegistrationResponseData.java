package com.fixit.rest.responses.data;

import com.fixit.data.OrderData;

import java.util.Arrays;

/**
 * Created by konstantin on 5/15/2017.
 */

public class UserRegistrationResponseData {

    private boolean existingEmail;
    private boolean newUser;
    private String userId;
    private OrderData[] orderHistory;

    public boolean isExistingEmail() {
        return existingEmail;
    }

    public void setExistingEmail(boolean existingEmail) {
        this.existingEmail = existingEmail;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OrderData[] getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(OrderData[] orderHistory) {
        this.orderHistory = orderHistory;
    }

    @Override
    public String toString() {
        return "UserRegistrationResponseData{" +
                "existingEmail=" + existingEmail +
                ", newUser=" + newUser +
                ", userId='" + userId + '\'' +
                ", orderHistory=" + Arrays.toString(orderHistory) +
                '}';
    }
}
