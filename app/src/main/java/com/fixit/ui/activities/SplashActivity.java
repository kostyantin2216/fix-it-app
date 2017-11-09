package com.fixit.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fixit.app.R;
import com.fixit.general.IntentHandler;
import com.fixit.utils.CommonUtils;

public class SplashActivity extends LauncherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onAppReady() {
        if(!IntentHandler.handle(this)) {
            startActivity(new Intent(this, SplitSearchActivity.class));
            finish();
        }
    }

}
