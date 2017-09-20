package com.fixit.external.google;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by konstantin on 5/14/2017.
 */

public class FacebookClientManager implements FacebookCallback<LoginResult> {

    private final LoginManager mLoginManager;
    private final CallbackManager mCallbackManager;

    private FacebookLoginCallback mLoginCallback;

    public FacebookClientManager(LoginManager loginManager, CallbackManager callbackManager) {
        mLoginManager = loginManager;
        mCallbackManager = callbackManager;

        mLoginManager.registerCallback(mCallbackManager, this);
    }

    public void login(Fragment fragment, FacebookLoginCallback loginCallback) {
        mLoginCallback = loginCallback;
        mLoginManager.logInWithReadPermissions(fragment, Arrays.asList("public_profile", "email"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        Profile profile = Profile.getCurrentProfile();
        if(profile == null) {
            new ProfileTracker() {

                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    this.stopTracking();
                    makeGraphRequest(currentProfile, loginResult.getAccessToken());
                }
            };
        } else {
            makeGraphRequest(profile, loginResult.getAccessToken());
        }
    }

    private void makeGraphRequest(final Profile profile, AccessToken accessToken) {
        String[] requiredFields = new String[]{ "email" };
        Bundle parameters = new Bundle();
        parameters.putString("fields", TextUtils.join(",", requiredFields));

        GraphRequest request = new GraphRequest(accessToken, "me", parameters, null, new GraphRequest.Callback() {

            @Override
            public void onCompleted(GraphResponse response) {
                if (response.getError() != null) {
                    mLoginCallback.onLogInError(null);
                } else {
                    JSONObject me = response.getJSONObject();
                    String email = me.optString("email");
                    mLoginCallback.onLogInSuccess(profile, email);
                }
                mLoginCallback = null;
            }
        });

        request.executeAsync();
    }

    @Override
    public void onCancel() {
        mLoginCallback.onLogInCanceled();
        mLoginCallback = null;
    }

    @Override
    public void onError(FacebookException error) {
        mLoginCallback.onLogInError(error);
        mLoginCallback = null;
    }

    public interface FacebookLoginCallback {
        void onLogInSuccess(Profile profile, String email);
        void onLogInCanceled();
        void onLogInError(Throwable t);
    }

}
