package com.fixit.app.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.LoginFragment;
import com.fixit.app.ui.fragments.TelephoneVerificationFragment;
import com.fixit.app.ui.fragments.UserRegistrationFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.RegistrationController;
import com.fixit.core.controllers.UserController;
import com.fixit.core.data.UserAccountDetails;
import com.fixit.core.utils.PrefUtils;

/**
 * Created by konstantin on 5/11/2017.
 */

public class LoginActivity extends BaseAppActivity<RegistrationController>
    implements LoginFragment.LoginFragmentCallbacks,
               UserRegistrationFragment.UserRegistrationInteractionsListener,
               TelephoneVerificationFragment.TelephoneVerificationListener,
               UserController.UserRegistrationCallback {

    private final static String FRAG_TAG_NUMBER_VERIFICATION = "number_verification";

    private UserAccountDetails mUserAccountDetails;

    private boolean isGoogleLoginEnabled = true;
    private boolean isFacebookLoginEnabled = true;

    private boolean allowBackPress = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserAccountDetails = new UserAccountDetails();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_container, LoginFragment.newInstance())
                .commit();

        setActivityBackPressPrompt(new ActivityBackPressPrompt(
                getString(R.string.question_exit_without_login),
                getString(R.string.yes), getString(R.string.no)
        ));
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
        return new RegistrationController((BaseApplication) getApplication(), this);
    }

    @Override
    public void onLoggedIn(String firstName, String lastName, String email, String avatarUrl) {
        onLoggedIn(firstName, lastName, email, avatarUrl, null, null);
    }

    @Override
    public void onLoggedIn(String firstName, String lastName, String email, String avatarUrl, String googleId, String facebookId) {
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
    public void onRegistrationSuccess(String userId) {
        PrefUtils.setUserId(this, userId);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void emailAlreadyExists() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_container, LoginFragment.newInstance())
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
