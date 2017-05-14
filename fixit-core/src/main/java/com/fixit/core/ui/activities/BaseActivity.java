package com.fixit.core.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fixit.core.R;
import com.fixit.core.config.AppConfig;
import com.fixit.core.controllers.ActivityController;
import com.fixit.core.general.UnexpectedErrorCallback;
import com.fixit.core.rest.APIError;
import com.fixit.core.rest.callbacks.AppServiceErrorCallback;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.ui.fragments.ErrorFragment;

import java.util.List;


/**
 * Created by Kostyantin on 12/21/2016.
 */

public abstract class BaseActivity<C extends ActivityController> extends AppCompatActivity
        implements BaseFragment.BaseFragmentInteractionsListener,
                   AppServiceErrorCallback,
                   UnexpectedErrorCallback {

    private C mController;
    private MaterialDialog mLoaderDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mController = createController();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isNetworkConnected()) {
            showError(ErrorFragment.ErrorType.NO_NETWORK);
        }
    }

    public C getController() {
        return mController;
    }

    @Override
    public void setToolbar(Toolbar toolbar, boolean homeAsUpEnabled) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    @Override
    public void startChrome(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);

            try {
                startActivity(intent);
            } catch(ActivityNotFoundException innerEx) {
                // No web browser apps installed.

                notifyUser(getString(R.string.no_internet_browser));
            }
        }
    }

    @Override
    public void hideKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /**
     *  Override for enabling/disabling user notifications.
     */
    public boolean notifyPossible() {
        return true;
    }

    @Override
    public void notifyUser(String msg) {
        if(notifyPossible()) {
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(AppConfig.getColor(this, AppConfig.KEY_COLOR_ACCENT, Color.BLACK));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.large_text_size));
            textView.setMaxLines(5);
            snackbar.show();
        }
    }

    @Override
    public void onAppServiceError(List<APIError> errors) {
        showError(ErrorFragment.ErrorType.GENERAL.createBuilder(this).apiError(errors).build());
    }

    @Override
    public void onUnexpectedErrorOccurred(String msg, Throwable t) {
        showError(ErrorFragment.ErrorType.GENERAL.createBuilder(this).log(msg).cause(t).build());
    }

    public void showError() {
        showError(ErrorFragment.ErrorType.GENERAL);
    }

    @Override
    public void showError(String error) {
        showError(ErrorFragment.ErrorType.GENERAL, error);
    }

    public void showError(ErrorFragment.ErrorType errorType) {
        showError(errorType.createBuilder(this).build());
    }

    public void showError(ErrorFragment.ErrorType errorType, String msg) {
        showError(errorType.createBuilder(msg).build());
    }

    public void showError(ErrorFragment.ErrorParams params) {
        hideLoader();
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, ErrorFragment.newInstance(params))
                .commit();
    }

    @Override
    public void showPrompt(String message) {
        showError(ErrorFragment.ErrorType.PROMPT.createBuilder(message).build());
    }

    public void showLoader() {
        showLoader(getString(R.string.loading));
    }

    public void showLoader(String message) {
        if(mLoaderDialog == null) {
            mLoaderDialog = new MaterialDialog.Builder(this)
                    .title(R.string.please_wait)
                    .content(message)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .show();
        } else {
            mLoaderDialog.setContent(message);
            mLoaderDialog.show();
        }
    }

    public void hideLoader() {
        if(mLoaderDialog != null) {
            mLoaderDialog.dismiss();
            mLoaderDialog = null;
        }
    }

    public abstract C createController();

    // Fragments

    @Nullable
    public <T extends Fragment> T getFragment(String tag, Class<T> fragmentClass) {
        return getFragment(tag, fragmentClass, false, 0);
    }

    @Nullable
    public <T extends Fragment> T getFragment(String tag, Class<T> fragmentClass, boolean createIfNotExist) {
        return getFragment(tag, fragmentClass, createIfNotExist, android.R.id.content);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends Fragment> T getFragment(String tag, Class<T> fragmentClass, boolean createIfNotExist, int containerViewId) {
        FragmentManager fm = getSupportFragmentManager();

        T fragment = (T) fm.findFragmentByTag(tag);
        if(createIfNotExist && fragment == null) {
            fragment = (T) Fragment.instantiate(this, fragmentClass.getCanonicalName());

            fm.beginTransaction()
                    .add(containerViewId, fragment, tag)
                    .commit();
        }

        return fragment;
    }

}
