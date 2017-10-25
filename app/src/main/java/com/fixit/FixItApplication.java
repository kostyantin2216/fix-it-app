package com.fixit;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.fixit.caching.ApplicationCache;
import com.fixit.config.AppConfig;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;
import com.fixit.general.AnalyticsManager;
import com.fixit.utils.FILog;

import io.fabric.sdk.android.Fabric;

/**
 * Created by konstantin on 3/29/2017.
 */

public class FixItApplication extends Application {

    private AnalyticsManager mAnalyticsManager;
    private ApplicationCache mAppCache;
    private APIFactory mServerApiFactory;
    private DAOFactory mDaoFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        FILog.i("running version: " + AppConfig.getVersionInfo(this).getName());
    }

    public AnalyticsManager getAnalyticsManager() {
        if(mAnalyticsManager == null) {
            mAnalyticsManager = new AnalyticsManager(getApplicationContext());
        }
        return mAnalyticsManager;
    }

    public APIFactory getServerApiFactory() {
        if(mServerApiFactory == null) {
            mServerApiFactory = new APIFactory(this);
        }

        return mServerApiFactory;
    }

    public DAOFactory getDaoFactory() {
        if(mDaoFactory == null) {
            mDaoFactory = new DAOFactory(this);
        }

        return mDaoFactory;
    }

    public ApplicationCache getAppCache() {
        if(mAppCache == null) {
            mAppCache = new ApplicationCache(getServerApiFactory());
        }
        return mAppCache;
    }

    public void onDeveloperSettingsChanged() {
        mServerApiFactory = null;
        mDaoFactory = null;
    }
}
