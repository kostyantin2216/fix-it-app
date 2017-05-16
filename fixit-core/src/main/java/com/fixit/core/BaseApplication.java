package com.fixit.core;

import android.app.Application;

import com.fixit.core.factories.DAOFactory;
import com.fixit.core.factories.APIFactory;

/**
 * Created by Kostyantin on 12/21/2016.
 */

public class BaseApplication extends Application {

    private APIFactory mServerApiFactory;
    private DAOFactory mDaoFactory;

    @Override
    public void onCreate() {
        super.onCreate();
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

}
