package com.fixit.controllers;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.fixit.FixItApplication;
import com.fixit.data.OrderData;
import com.fixit.data.UserAccountDetails;
import com.fixit.rest.apis.UserServiceAPI;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.rest.callbacks.ManagedServiceCallback;
import com.fixit.rest.requests.data.UserRegistrationRequestData;
import com.fixit.rest.responses.data.UserRegistrationResponseData;
import com.fixit.utils.GlobalPreferences;

/**
 * Created by konstantin on 5/8/2017.
 */

public class UserController extends OrderController {

    private final UserServiceAPI mUserApi;

    public UserController(FixItApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);
        mUserApi = getServerApiFactory().createUserServiceApi();
    }

    public boolean isUserRegistered() {
        return !TextUtils.isEmpty(GlobalPreferences.getUserId(getApplicationContext()));
    }

    public void registerUser(UserAccountDetails accountDetails, final UserRegistrationCallback callback) {
        UserRegistrationRequestData requestData = new UserRegistrationRequestData(
                accountDetails.getFirstName(),
                accountDetails.getLastName(),
                accountDetails.getEmail(),
                accountDetails.getTelephone(),
                accountDetails.getAvatarUrl(),
                accountDetails.getGoogleId(),
                accountDetails.getFacebookId()
        );

        mUserApi.registerUser(requestData, new ManagedServiceCallback<UserRegistrationResponseData>(getApplicationContext(), callback, "could not register user") {
            @Override
            public void onResponse(UserRegistrationResponseData responseData) {
                if(responseData.isExistingEmail()) {
                    callback.emailAlreadyExists();
                } else {
                    String userId = responseData.getUserId();
                    Crashlytics.setUserIdentifier(userId);
                    callback.onRegistrationSuccess(userId);
                    getServerApiFactory().updateUserId(userId);

                    OrderData[] orderData = responseData.getOrderHistory();
                    if(orderData != null && orderData.length > 0) {
                        saveOrders(orderData);
                    }
                }
            }
        });
    }

    public interface UserRegistrationCallback extends GeneralServiceErrorCallback {
        void onRegistrationSuccess(String userId);
        void emailAlreadyExists();
    }

}
