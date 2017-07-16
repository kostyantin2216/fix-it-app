package com.fixit.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.fixit.app.R;
import com.fixit.core.ui.activities.LauncherActivity;

public class SplashActivity extends LauncherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onAppReady() {
        startActivity(new Intent(this, SearchActivity.class));
        finish();
    }

}
