package com.fixit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.fixit.app.R;
import com.fixit.FixxitApplication;
import com.fixit.controllers.RegistrationController;
import com.fixit.controllers.UserController;
import com.fixit.data.UserAccountDetails;
import com.fixit.utils.Constants;
import com.fixit.utils.GlobalPreferences;
import com.fixit.ui.fragments.LoginFragment;
import com.fixit.ui.fragments.TelephoneVerificationFragment;
import com.fixit.ui.fragments.UserRegistrationFragment;

/**
 * Created by konstantin on 5/11/2017.
 */

public class LoginActivity extends BaseActivity<RegistrationController>
    implements LoginFragment.LoginFragmentCallbacks,
               UserRegistrationFragment.UserRegistrationInteractionsListener,
               TelephoneVerificationFragment.TelephoneVerificationListener,
               UserController.UserRegistrationCallback {

    private final static String FRAG_TAG_NUMBER_VERIFICATION = "number_verification";

    private UserAccountDetails mUserAccountDetails;

    private String mLoginMessage;

    private boolean isGoogleLoginEnabled = true;
    private boolean isFacebookLoginEnabled = true;

    private boolean allowBackPress = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserAccountDetails = new UserAccountDetails();

        Intent intent = getIntent();
        mLoginMessage = intent.getStringExtra(Constants.ARG_LOGIN_MESSAGE);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                    .add(R.id.fragment_container, LoginFragment.newInstance(mLoginMessage))
                    .commit();
        }

        String backPressPromptMessage = intent.getStringExtra(Constants.ARG_PROMPT_ON_BACK_PRESS_MESSAGE);
        if(!TextUtils.isEmpty(backPressPromptMessage)) {
            setActivityBackPressPrompt(new ActivityBackPressPrompt(
                    backPressPromptMessage, getString(R.string.yes), getString(R.string.no)
            ));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(allowBackPress) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAG_TAG_NUMBER_VERIFICATION);
            if (fragment != null) {
                registerOnBackPressListener((OnBackPressListener) fragment);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        clearOnBackPressListeners();
    }

    @Override
    public RegistrationController createController() {
        return new RegistrationController((FixxitApplication) getApplication(), this);
    }

    @Override
    public void onLoggedIn(String firstName, String lastName, String email, String avatarUrl) {
        onLoggedIn(UserAccountDetails.SignUpMethod.MANUAL, firstName, lastName, email, avatarUrl, null, null);
    }

    @Override
    public void onLoggedIn(UserAccountDetails.SignUpMethod method, String firstName, String lastName, String email,
                           String avatarUrl, String googleId, String facebookId) {
        mUserAccountDetails.setSignUpMethod(method);
        mUserAccountDetails.setFirstName(firstName);
        mUserAccountDetails.setLastName(lastName);
        mUserAccountDetails.setEmail(email);
        mUserAccountDetails.setAvatarUrl(avatarUrl);
        mUserAccountDetails.setGoogleId(googleId);
        mUserAccountDetails.setFacebookId(facebookId);

        beginVerification();
    }

    @Override
    public void updateAccountDetails(String firstName, String lastName, String email) {
        mUserAccountDetails.setFirstName(firstName);
        mUserAccountDetails.setLastName(lastName);
        mUserAccountDetails.setEmail(email);
    }

    @Override
    public void beginRegistration() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_right, R.anim.exit_out_left)
                .replace(R.id.fragment_container, UserRegistrationFragment.newInstance(mUserAccountDetails, false))
                .commit();
    }

    public void beginVerification() {
        clearFragmentBackStack();
        TelephoneVerificationFragment fragment = TelephoneVerificationFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .replace(R.id.fragment_container, fragment, FRAG_TAG_NUMBER_VERIFICATION)
                .commit();

        registerOnBackPressListener(fragment);
    }

    @Override
    public void onTelephoneNumberVerified(String telephone) {
        mUserAccountDetails.setTelephone(telephone);
        RegistrationController controller = getController();
        controller.numberVerified();
        controller.registerUser(mUserAccountDetails, this);
        allowBackPress = false;
        clearOnBackPressListeners();
    }

    @Override
    public void onRegistrationSuccess(boolean newUser, String userId) {
        GlobalPreferences.setUserId(this, userId);
        getAnalyticsManager().login(this, newUser, mUserAccountDetails.getSignUpMethod().name());
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void emailAlreadyExists() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_container, LoginFragment.newInstance(mLoginMessage))
                .addToBackStack(null)
                .replace(R.id.fragment_container, UserRegistrationFragment.newInstance(mUserAccountDetails, true))
                .commit();
    }

    @Override
    public void setGoogleLoginEnabled(boolean enabled) {
        isGoogleLoginEnabled = enabled;
    }

    @Override
    public void setFacebookLoginEnabled(boolean enabled) {
        isFacebookLoginEnabled = enabled;
    }

    @Override
    public boolean isGoogleLoginEnabled() {
        return isGoogleLoginEnabled;
    }

    @Override
    public boolean isFacebookLoginEnabled() {
        return isFacebookLoginEnabled;
    }
}
