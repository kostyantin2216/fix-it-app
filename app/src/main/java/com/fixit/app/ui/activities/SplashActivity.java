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

        /*Bundle extras = new Bundle();
        extras.putParcelableArrayList(Constants.ARG_TRADESMEN, ObjectGenerator.createTradesmenWrappers(3));

        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtras(extras);

        startActivity(intent);*/
    }

    @Override
    public void onAppReady() {
        startActivity(new Intent(this, SearchActivity.class));
        finish();
    }

}
