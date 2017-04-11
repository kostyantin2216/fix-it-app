package com.fixit.core.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.fixit.core.R;
import com.fixit.core.config.AppConfig;
import com.fixit.core.controllers.ActivityController;
import com.fixit.core.general.UnexpectedErrorCallback;
import com.fixit.core.rest.APIError;
import com.fixit.core.rest.callbacks.AppServiceErrorCallback;
import com.fixit.core.ui.fragments.BaseFragment;

import java.util.List;


/**
 * Created by Kostyantin on 12/21/2016.
 */

public abstract class BaseActivity<C extends ActivityController> extends AppCompatActivity
        implements BaseFragment.BaseFragmentInteractionsListener,
                   AppServiceErrorCallback,
                   UnexpectedErrorCallback {

    private C mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mController = createController();
        super.onCreate(savedInstanceState);
    }

    public C getController() {
        return mController;
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

                showError("");
            }
        }
    }

    @Override
    public void hideKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
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
        // TODO:
    }

    @Override
    public void onUnexpectedErrorOccurred(String msg, Throwable t) {
        // TODO:
    }

    public void showError(String error) {
        // TODO:
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
