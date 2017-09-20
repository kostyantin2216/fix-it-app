package com.fixit.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.fixit.app.R;
import com.fixit.controllers.ActivityController;
import com.fixit.ui.fragments.StaticWebPageFragment;
import com.fixit.utils.Constants;

/**
 * Created by Kostyantin on 9/16/2017.
 */

public class WebActivity extends BaseActivity<ActivityController> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString(Constants.ARG_TITLE);

        if(!TextUtils.isEmpty(title)) {
            setContentView(R.layout.layout_appbar_fragment_holder);
            setToolbar((Toolbar) findViewById(R.id.toolbar), true);
            setToolbarTitle(title);
        } else {
            setContentView(R.layout.layout_fragment_holder);
        }

        if(savedInstanceState == null) {
            String url = extras.getString(Constants.ARG_URL);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                    .add(R.id.fragment_holder, StaticWebPageFragment.newFragment(title, url))
                    .commit();
        }
    }

    @Override
    public ActivityController createController() {
        return null;
    }
}
