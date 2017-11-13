package com.fixit.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fixit.FixxitApplication;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;

/**
 * Created by konstantin on 7/19/2017.
 */

public class BaseBroadcastReceiver extends BroadcastReceiver {

    private FixxitApplication mApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        mApplication = (FixxitApplication) context.getApplicationContext();
    }

    public APIFactory getAPIFactory() {
        return mApplication.getServerApiFactory();
    }

    public DAOFactory getDAOFactory() {
        return mApplication.getDaoFactory();
    }

}
