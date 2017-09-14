package com.fixit.controllers;

import android.content.Context;

import com.fixit.FixItApplication;
import com.fixit.caching.ApplicationCache;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;
import com.fixit.general.AnalyticsManager;
import com.fixit.rest.ServerCallback;
import com.fixit.ui.fragments.ErrorFragment;

/**
 * Created by konstantin on 2/20/2017.
 */

abstract class BaseController implements ActivityController, ServerCallback {

    private final FixItApplication mFixItApplication;
    private final UiCallback mUiCallback;

    public BaseController(FixItApplication baseApplication, UiCallback uiCallback) {
        mFixItApplication = baseApplication;
        mUiCallback = uiCallback;
    }

    public Context getApplicationContext() {
        return mFixItApplication;
    }

    public UiCallback getUiCallback(){
        return mUiCallback;
    }

    @Override
    public DAOFactory getDaoFactory() {
        return mFixItApplication.getDaoFactory();
    }

    @Override
    public APIFactory getServerApiFactory() {
        return mFixItApplication.getServerApiFactory();
    }

    @Override
    public AnalyticsManager getAnalyticsManager() {
        return mFixItApplication.getAnalyticsManager();
    }

    @Override
    public ApplicationCache getAppCache() {
        return mFixItApplication.getAppCache();
    }

    @Override
    public void serverUnavailable() {
        mUiCallback.showError(ErrorFragment.ErrorType.SERVER_UNAVAILABLE);
    }

}
