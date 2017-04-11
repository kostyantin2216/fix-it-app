package com.fixit.core;

import android.app.Application;

import com.fixit.core.database.DatabaseManager;
import com.fixit.core.factories.DAOFactory;
import com.fixit.core.factories.ServerAPIFactory;

/**
 * Created by Kostyantin on 12/21/2016.
 */

public class BaseApplication extends Application {



    private ServerAPIFactory mServerApiFactory;
    private DAOFactory mDaoFactory;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ServerAPIFactory getServerApiFactory() {
        if(mServerApiFactory == null) {
            mServerApiFactory = new ServerAPIFactory(this);
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
