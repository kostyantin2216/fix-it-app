package com.fixit.general;

import com.appsflyer.AppsFlyerLib;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Kostyantin on 11/11/2017.
 */

public class FixxitInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseInstanceId instance = FirebaseInstanceId.getInstance();
        if (instance != null) {
            AppsFlyerLib.getInstance().updateServerUninstallToken(getApplicationContext(), instance.getToken());
        }
    }
}