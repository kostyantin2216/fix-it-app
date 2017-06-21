package com.fixit.app.ui.activities;

import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.core.controllers.ActivityController;
import com.fixit.core.ui.activities.BaseActivity;

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
}
