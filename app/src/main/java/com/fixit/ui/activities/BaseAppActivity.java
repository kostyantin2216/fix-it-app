package com.fixit.ui.activities;

import android.content.Intent;
import android.util.TypedValue;
import android.widget.TextView;

import com.fixit.FixItApplication;
import com.fixit.app.R;
import com.fixit.controllers.ActivityController;
import com.fixit.general.AnalyticsManager;

/**
 * Created by Kostyantin on 5/26/2017.
 */

public abstract class BaseAppActivity<C extends ActivityController> extends BaseActivity<C> {

    @Override
    public Class<?> getLoginActivity() {
        return LoginActivity.class;
    }

    @Override
    public void setToolbarTitle(String title) {
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        if(toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    public void setToolbarTitleTextSize(float sp) {
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        if(toolbarTitle != null) {
            toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        }
    }

    @Override
    public void openDeveloperSettings() {
        startActivity(new Intent(this, DeveloperSettingsActivity.class));
    }

    @Override
    public void restartApp(boolean skipSplash) {
        Intent intent;
        if(skipSplash) {
            intent = new Intent(this, SearchActivity.class);
        } else {
            intent = new Intent(this, SplashActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public AnalyticsManager getAnalyticsManager() {
        return ((FixItApplication) getApplication()).getAnalyticsManager();
    }
}
