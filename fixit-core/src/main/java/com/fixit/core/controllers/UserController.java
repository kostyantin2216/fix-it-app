package com.fixit.core.controllers;

import android.text.TextUtils;

import com.fixit.core.BaseApplication;
import com.fixit.core.data.UserAccountDetails;
import com.fixit.core.rest.apis.UserServiceAPI;
import com.fixit.core.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.core.rest.callbacks.ManagedServiceCallback;
import com.fixit.core.rest.requests.data.UserRegistrationRequestData;
import com.fixit.core.rest.responses.data.UserRegistrationResponseData;
import com.fixit.core.utils.GlobalPreferences;

/**
 * Created by konstantin on 5/8/2017.
 */

public class UserController extends BaseController {

    private final UserServiceAPI mUserApi;

    public UserController(BaseApplication baseApplication, UiCallback uiCallback) {
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
                    callback.onRegistrationSuccess(userId);
                    getServerApiFactory().updateUserId(userId);
                }
            }
        });
    }

    public interface UserRegistrationCallback extends GeneralServiceErrorCallback {
        void onRegistrationSuccess(String userId);
        void emailAlreadyExists();
    }

}
