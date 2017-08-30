package com.fixit;

import android.app.Application;

import com.fixit.caching.ApplicationCache;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;

/**
 * Created by Kostyantin on 12/21/2016.
 */

public class BaseApplication extends Application {

    private ApplicationCache mAppCache;
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
