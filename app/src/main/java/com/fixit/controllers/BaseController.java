package com.fixit.controllers;

import android.content.Context;

import com.fixit.BaseApplication;
import com.fixit.caching.ApplicationCache;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;
import com.fixit.rest.ServerCallback;
import com.fixit.ui.fragments.ErrorFragment;

/**
 * Created by konstantin on 2/20/2017.
 */

abstract class BaseController implements ActivityController, ServerCallback {

    private final BaseApplication mBaseApplication;
    private final UiCallback mUiCallback;

    public BaseController(BaseApplication baseApplication, UiCallback uiCallback) {
        mBaseApplication = baseApplication;
        mUiCallback = uiCallback;
    }

    public Context getApplicationContext() {
        return mBaseApplication;
    }

    public UiCallback getUiCallback(){
        return mUiCallback;
    }

    @Override
    public DAOFactory getDaoFactory() {
        return mBaseApplication.getDaoFactory();
    }

    @Override
    public APIFactory getServerApiFactory() {
        return mBaseApplication.getServerApiFactory();
    }

    @Override
    public ApplicationCache getAppCache() {
        return mBaseApplication.getAppCache();
    }

    @Override
    public void serverUnavailable() {
        mUiCallback.showError(ErrorFragment.ErrorType.SERVER_UNAVAILABLE);
    }

}
