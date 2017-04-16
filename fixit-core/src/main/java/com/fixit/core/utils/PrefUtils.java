package com.fixit.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fixit.core.data.ServerLog;
import com.fixit.core.rest.requests.APIRequestHeader;

/**
 * Created by Kostyantin on 12/24/2016.
 */

public class PrefUtils {

    public final static String PREF_GROUP_GLOBAL = "global_prefs";
    public final static String PREF_USER_ID = "pref_user_id";
    public final static String PREF_INSTALLATION_ID = "pref_installation_id";

    public static String getUserId(Context context) {
        return context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .getString(PREF_USER_ID, "");
    }

    public static String getInstallationId(Context context) {
        String installationId = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .getString(PREF_INSTALLATION_ID, "");
        return installationId;
    }

    public static void setInstallationId(Context context, String installationId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .edit().putString(PREF_INSTALLATION_ID, installationId);
        editor.apply();
    }

    public static void fillApiRequestHeader(Context context, APIRequestHeader header) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE);
        header.setUserId(prefs.getString(PREF_USER_ID, ""));
        header.setInstallationId(prefs.getString(PREF_INSTALLATION_ID, ""));
    }

    public static void fillServerLog(Context context, ServerLog log) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE);
        log.setUserId(prefs.getString(PREF_USER_ID, ""));
        log.setInstallationId(prefs.getString(PREF_INSTALLATION_ID, ""));
    }

}
