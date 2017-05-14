package com.fixit.app.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fixit.app.R;
import com.fixit.app.ifs.external.google.FacebookClientManager;
import com.fixit.app.ifs.external.google.GoogleClientManager;
import com.fixit.core.controllers.UserController;
import com.fixit.core.ui.fragments.BaseFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

/**
 * Created by konstantin on 5/8/2017.
 */

public class LoginFragment extends BaseFragment<UserController>
    implements View.OnClickListener,
               GoogleClientManager.GoogleManagerCallback,
               GoogleClientManager.GoogleSignInCallback,
               FacebookClientManager.FacebookLoginCallback {

    private LoginFragmentCallbacks mCallbacks;

    private FacebookClientManager mFacebookClient;
    private GoogleClientManager mGoogleClient;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFacebookClient = new FacebookClientManager(LoginManager.getInstance(), CallbackManager.Factory.create());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleClient = new GoogleClientManager(
                new GoogleApiClient.Builder(getContext())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso),
                getActivity(),
                this
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        v.findViewById(R.id.btn_fb_login).setOnClickListener(this);
        v.findViewById(R.id.btn_google_login).setOnClickListener(this);
        v.findViewById(R.id.btn_register).setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookClient.onActivityResult(requestCode, resultCode, data);
        mGoogleClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof LoginFragmentCallbacks) {
            mCallbacks = (LoginFragmentCallbacks) context;
        } else {
            throw new IllegalStateException("context must implement "
                        + LoginFragmentCallbacks.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fb_login:
                loginWithFacebook();
                break;
            case R.id.btn_google_login:
                loginWithGoogle();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void loginWithFacebook() {
        mFacebookClient.login(this, this);
    }

    private void loginWithGoogle() {
        mGoogleClient.login(this, this);
    }

    private void register() {
        if(mCallbacks != null) {
            mCallbacks.beginRegistration();
        }
    }

    // GOOGLE LOGIN CALLBACKS
    @Override
    public void onSignInSuccess(GoogleSignInAccount account) {
        if(mCallbacks != null) {
            Uri photoUrl = account.getPhotoUrl();
            mCallbacks.onLoggedIn(
                    account.getGivenName(),
                    account.getFamilyName(),
                    account.getEmail(),
                    photoUrl != null ? photoUrl.toString() : null
            );
        }
    }

    @Override
    public void onSignInError() {
        onUnexpectedErrorOccurred("cannot login with google", null);
    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment, String tag) {
        dialogFragment.show(getFragmentManager(), tag);
    }

    @Override
    public Activity getActivityForResult() {
        return getActivity();
    }

    // FACEBOOK LOGIN CALLBACKS
    @Override
    public void onLogInSuccess(Profile profile, String email) {
        if(mCallbacks != null) {
            Uri photoUri =  profile.getProfilePictureUri(200, 200);
            mCallbacks.onLoggedIn(
                    profile.getFirstName(),
                    profile.getLastName(),
                    email,
                    photoUri != null ? photoUri.toString() : null
            );
        }
    }

    @Override
    public void onLogInCanceled() {
        // do nothing, user needs to login or register.
    }

    @Override
    public void onLogInError() {
        onUnexpectedErrorOccurred("cannot login with facebook", null);
    }

    public interface LoginFragmentCallbacks {
        void onLoggedIn(String firstName, String lastName, String email, String avatarUrl);
        void beginRegistration();
    }
}
