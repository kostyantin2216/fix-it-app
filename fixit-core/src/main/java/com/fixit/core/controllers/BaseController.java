package com.fixit.core.controllers;

import android.content.Context;

import com.fixit.core.BaseApplication;
import com.fixit.core.factories.DAOFactory;
import com.fixit.core.factories.APIFactory;
import com.fixit.core.rest.ServerCallback;
import com.fixit.core.ui.fragments.ErrorFragment;

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
    public void serverUnavailable() {
        mUiCallback.showError(ErrorFragment.ErrorType.SERVER_UNAVAILABLE);
    }

}
