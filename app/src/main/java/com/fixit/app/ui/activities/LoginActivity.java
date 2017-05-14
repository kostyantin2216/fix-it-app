package com.fixit.app.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.LoginFragment;
import com.fixit.app.ui.fragments.UserRegistrationFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.UserController;
import com.fixit.core.ui.activities.BaseActivity;

/**
 * Created by konstantin on 5/11/2017.
 */

public class LoginActivity extends BaseActivity<UserController>
    implements LoginFragment.LoginFragmentCallbacks {

    private LoginDetails mLoginDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_container, LoginFragment.newInstance())
                .commit();
    }

    @Override
    public UserController createController() {
        return new UserController((BaseApplication) getApplication());
    }

    @Override
    public void onLoggedIn(String firstName, String lastName, String email, String avatarUrl) {
        mLoginDetails.firstName = firstName;
        mLoginDetails.lastName = lastName;
        mLoginDetails.email = email;
        mLoginDetails.avatarUrl = avatarUrl;
    }

    @Override
    public void beginRegistration() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .replace(R.id.fragment_container, UserRegistrationFragment.newInstance())
                .commit();
    }

    private static class LoginDetails {
        String firstName;
        String lastName;
        String email;
        String telephone;
        String avatarUrl;
    }
}
