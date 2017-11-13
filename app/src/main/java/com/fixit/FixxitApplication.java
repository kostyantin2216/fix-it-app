package com.fixit;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.ConversionDataListener;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.fixit.app.R;
import com.fixit.caching.ApplicationCache;
import com.fixit.config.AppConfig;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;
import com.fixit.general.AnalyticsManager;
import com.fixit.utils.FILog;

import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by konstantin on 3/29/2017.
 */

public class FixxitApplication extends Application implements AppsFlyerConversionListener {

    private AnalyticsManager mAnalyticsManager;
    private ApplicationCache mAppCache;
    private APIFactory mServerApiFactory;
    private DAOFactory mDaoFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        FILog.i("running version: " + AppConfig.getVersionInfo(this).getName());

        AppsFlyerLib.getInstance().init(getString(R.string.apps_flyer_key), this);
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

    @Override
    public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
        for (String attrName : conversionData.keySet()) {
            FILog.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
        }
    }

    @Override
    public void onInstallConversionFailure(String errorMessage) {
        FILog.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
    }

    @Override
    public void onAppOpenAttribution(Map<String, String> conversionData) { }

    @Override
    public void onAttributionFailure(String errorMessage) {
        FILog.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
    }
}
