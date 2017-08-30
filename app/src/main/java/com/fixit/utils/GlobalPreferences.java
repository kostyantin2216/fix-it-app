package com.fixit.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.fixit.data.ServerLog;
import com.fixit.rest.requests.APIRequestHeader;

import java.util.UUID;

/**
 * Created by Kostyantin on 12/24/2016.
 */

public class GlobalPreferences {

    private final static String PREF_GROUP_GLOBAL = "global_prefs";
    private final static String PREF_USER_ID = "pref_user_id";
    private final static String PREF_INSTALLATION_ID = "pref_installation_id";
    private final static String PREF_DEVICE_ID = "pref_device_id";
    private final static String PREF_LAST_ORDER_ID = "pref_last_order_id";

    // GETTERS

    public static String getUserId(Context context) {
        return context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .getString(PREF_USER_ID, "");
    }

    public static String getInstallationId(Context context) {
        return context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .getString(PREF_INSTALLATION_ID, "");
    }

    public static String getLastOrderId(Context context) {
        return context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .getString(PREF_LAST_ORDER_ID, null);
    }

    public static String getDeviceId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE);
        String deviceId = prefs.getString(PREF_DEVICE_ID, null);

        if(deviceId == null) {
            deviceId = createDeviceId(context);
            prefs.edit().putString(PREF_DEVICE_ID, deviceId).apply();
        }

        return deviceId;
    }

    // SETTERS

    public static void setLastOrderId(Context context, String orderId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .edit().putString(PREF_LAST_ORDER_ID, orderId);
        editor.apply();
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .edit().putString(PREF_USER_ID, userId);
        editor.apply();
    }

    public static void setInstallationId(Context context, String installationId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_GROUP_GLOBAL, Context.MODE_PRIVATE)
                .edit().putString(PREF_INSTALLATION_ID, installationId);
        editor.apply();
    }

    // PRIVATE UTILITIES

    /**
     * The device id will be a factor of the the servers decision of what a revisiting user
     * is after app reinstall.
     *
     * This is known to not be the most accurate way of getting a unique device id
     * since it can change upon factory resets or on rooted phones.
     * <a href="http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2785493#2785493">
     *     Article Link</a>
     *
     * @param context used for getting a {@link TelephonyManager} and/or {@link ContentResolver}
     * @return the devices unique id.
     */
    @SuppressLint("HardwareIds")
    private static String createDeviceId(Context context) {
        /*String myAndroidDeviceId = null;

        use only if you have to use permission: READ_PHONE_STATE

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String telephonyDeviceId = telephony.getDeviceId();
        if (telephonyDeviceId != null) {
            myAndroidDeviceId = telephony.getDeviceId();
        } else {*/
        String myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if(TextUtils.isEmpty(myAndroidDeviceId)) {
            myAndroidDeviceId = Build.SERIAL;

            if(TextUtils.isEmpty(myAndroidDeviceId)) {
                myAndroidDeviceId = "351" + //we make this look like a valid IMEI
                        Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                        Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                        Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                        Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                        Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                        Build.USER.length() % 10 + Build.DEVICE.length() % 10;
            }
        }

        return myAndroidDeviceId;
    }


    private String createUniqueID(Context context) {
        String uniqueId = "";
        uniqueId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if(TextUtils.isEmpty(uniqueId)
                || uniqueId.toLowerCase().contains("android")){
            uniqueId = UUID.randomUUID().toString().replace("-","");
        }
        return uniqueId;
    }


    // PUBLIC UTILITIES

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
