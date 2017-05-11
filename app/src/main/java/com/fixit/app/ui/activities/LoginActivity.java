package com.fixit.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.fixit.app.R;
import com.fixit.app.ifs.external.google.GoogleClientManager;
import com.fixit.app.ui.fragments.LoginFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.UserController;
import com.fixit.core.ui.activities.BaseActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by konstantin on 5/11/2017.
 */

public class LoginActivity extends BaseActivity<UserController>
    implements GoogleClientManager.GoogleManagerCallback {

    private GoogleClientManager mGoogleClientManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleClientManager = new GoogleClientManager(
                new GoogleApiClient.Builder(this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso),
                this,
                this
        );

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, LoginFragment.newInstance())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleClientManager.onActivityResult(requestCode, resultCode);
    }

    @Override
    public UserController createController() {
        return new UserController((BaseApplication) getApplication());
    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment, String tag) {
        dialogFragment.show(getSupportFragmentManager(), tag);
    }

    @Override
    public Activity getActivityForResult() {
        return this;
    }
}
