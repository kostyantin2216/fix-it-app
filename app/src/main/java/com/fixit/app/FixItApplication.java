package com.fixit.app;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.fixit.app.ifs.AnalyticsManager;
import com.fixit.core.BaseApplication;
import com.fixit.core.config.AppConfig;
import com.fixit.core.utils.FILog;

import io.fabric.sdk.android.Fabric;

/**
 * Created by konstantin on 3/29/2017.
 */

public class FixItApplication extends BaseApplication {

    private AnalyticsManager mAnalyticsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FILog.i("running version: " + AppConfig.getVersionInfo(this).getName());
    }

    public AnalyticsManager getAnalyticsManager() {
        if(mAnalyticsManager == null) {
            mAnalyticsManager = new AnalyticsManager(getApplicationContext());
        }
        return mAnalyticsManager;
    }
}
