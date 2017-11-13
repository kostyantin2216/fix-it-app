package com.fixit.controllers;

import android.content.Context;

import com.fixit.FixxitApplication;
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

    private final FixxitApplication mFixxitApplication;
    private final UiCallback mUiCallback;

    public BaseController(FixxitApplication baseApplication, UiCallback uiCallback) {
        mFixxitApplication = baseApplication;
        mUiCallback = uiCallback;
    }

    public Context getApplicationContext() {
        return mFixxitApplication;
    }

    public UiCallback getUiCallback(){
        return mUiCallback;
    }

    @Override
    public DAOFactory getDaoFactory() {
        return mFixxitApplication.getDaoFactory();
    }

    @Override
    public APIFactory getServerApiFactory() {
        return mFixxitApplication.getServerApiFactory();
    }

    @Override
    public AnalyticsManager getAnalyticsManager() {
        return mFixxitApplication.getAnalyticsManager();
    }

    @Override
    public ApplicationCache getAppCache() {
        return mFixxitApplication.getAppCache();
    }

    @Override
    public void serverUnavailable() {
        mUiCallback.showError(ErrorFragment.ErrorType.SERVER_UNAVAILABLE);
    }

}
