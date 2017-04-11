package com.fixit.core.controllers;

import android.content.Context;

import com.fixit.core.BaseApplication;
import com.fixit.core.database.DatabaseManager;
import com.fixit.core.factories.DAOFactory;
import com.fixit.core.factories.ServerAPIFactory;

/**
 * Created by konstantin on 2/20/2017.
 */

abstract class BaseController implements ActivityController {

    private final BaseApplication mBaseApplication;

    public BaseController(BaseApplication baseApplication) {
        mBaseApplication = baseApplication;
    }

    public Context getApplicationContext() {
        return mBaseApplication;
    }

    @Override
    public DAOFactory getDaoFactory() {
        return mBaseApplication.getDaoFactory();
    }

    @Override
    public ServerAPIFactory getServerApiFactory() {
        return mBaseApplication.getServerApiFactory();
    }
}
