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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.fixit.app.R;
import com.fixit.app.ifs.external.google.FacebookClientManager;
import com.fixit.app.ifs.external.google.GoogleClientManager;
import com.fixit.core.controllers.UserController;
import com.fixit.core.ui.fragments.BaseFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

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

    private ViewHolder mView;

    private static class ViewHolder {
        final TextView tvText;
        final ProgressBar pbLoader;
        final Button btnFacebookLogin;
        final Button btnGoogleLogin;
        final Button btnRegister;

        ViewHolder(View v, View.OnClickListener buttonClickListener) {
            tvText = (TextView) v.findViewById(R.id.tv_login_text);
            pbLoader = (ProgressBar) v.findViewById(R.id.loader);
            btnFacebookLogin = (Button) v.findViewById(R.id.btn_fb_login);
            btnGoogleLogin = (Button) v.findViewById(R.id.btn_google_login);
            btnRegister = (Button) v.findViewById(R.id.btn_register);

            btnFacebookLogin.setOnClickListener(buttonClickListener);
            btnGoogleLogin.setOnClickListener(buttonClickListener);
            btnRegister.setOnClickListener(buttonClickListener);
        }

        void showLoader() {
            tvText.setVisibility(View.GONE);
            pbLoader.setVisibility(View.VISIBLE);
            setClickable(false);
        }

        void hideLoader() {
            tvText.setVisibility(View.VISIBLE);
            pbLoader.setVisibility(View.GONE);
            setClickable(true);
        }

        void setClickable(boolean clickable) {
            btnFacebookLogin.setClickable(clickable);
            btnGoogleLogin.setClickable(clickable);
            btnRegister.setClickable(clickable);
        }
    }

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

        mView = new ViewHolder(v, this);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mCallbacks != null) {
            mView.btnFacebookLogin.setEnabled(mCallbacks.isFacebookLoginEnabled());
            mView.btnGoogleLogin.setEnabled(mCallbacks.isGoogleLoginEnabled());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        mView.showLoader();
        mFacebookClient.login(this, this);
    }

    private void loginWithGoogle() {
        mView.showLoader();
        mGoogleClient.login(this, this);
    }

    private void register() {
        if(mCallbacks != null) {
            mCallbacks.beginRegistration();
        }
    }

    private void completeLogin(String firstName, String lastName, String email, Uri avatarUrl, String googleId, String facebookId) {
        mView.hideLoader();
        if(mCallbacks != null) {
            mCallbacks.onLoggedIn(
                    firstName,
                    lastName,
                    email,
                    avatarUrl != null ? avatarUrl.toString() : null,
                    googleId,
                    facebookId
            );
        }
    }

    private void loginError(String forClient) {
        mView.hideLoader();
        String error = getString(R.string.login_failed_for, forClient);
        showPrompt(error);
    }

    // GOOGLE LOGIN CALLBACKS

    @Override
    public void onSignInSuccess(GoogleSignInAccount account) {
        completeLogin(
                account.getGivenName(),
                account.getFamilyName(),
                account.getEmail(),
                account.getPhotoUrl(),
                account.getId(),
                null
        );
    }

    @Override
    public void onSignInError(boolean wasCancelled) {
        mView.hideLoader();
        if(!wasCancelled) {
            mView.btnGoogleLogin.setEnabled(false);
            if (mCallbacks != null) {
                mCallbacks.setGoogleLoginEnabled(false);
            }
            loginError(getString(R.string.google));
        } else {
            notifyUser(getString(R.string.error_google_login));
        }
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
        completeLogin(
                profile.getFirstName(),
                profile.getLastName(),
                email,
                profile.getProfilePictureUri(200, 200),
                null,
                profile.getId()
        );
    }

    @Override
    public void onLogInCanceled() {
        mView.hideLoader();
        // do nothing, user needs to login or register.
    }

    @Override
    public void onLogInError() {
        mView.btnFacebookLogin.setEnabled(false);
        if(mCallbacks != null) {
            mCallbacks.setFacebookLoginEnabled(false);
        }
        loginError(getString(R.string.facebook));
    }

    public interface LoginFragmentCallbacks {
        void onLoggedIn(String firstName, String lastName, String email, String avatarUrl, String googleId, String facebookId);
        void beginRegistration();
        void setGoogleLoginEnabled(boolean enabled);
        void setFacebookLoginEnabled(boolean enabled);
        boolean isGoogleLoginEnabled();
        boolean isFacebookLoginEnabled();
    }
}
