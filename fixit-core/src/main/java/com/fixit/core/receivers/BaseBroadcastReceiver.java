package com.fixit.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fixit.core.BaseApplication;
import com.fixit.core.factories.APIFactory;
import com.fixit.core.factories.DAOFactory;

/**
 * Created by konstantin on 7/19/2017.
 */

public class BaseBroadcastReceiver extends BroadcastReceiver {

    private BaseApplication mApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        mApplication = (BaseApplication) context.getApplicationContext();
    }

    public APIFactory getAPIFactory() {
        return mApplication.getServerApiFactory();
    }

    public DAOFactory getDAOFactory() {
        return mApplication.getDaoFactory();
    }

}
