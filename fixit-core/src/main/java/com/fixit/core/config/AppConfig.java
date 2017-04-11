package com.fixit.core.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;

import com.fixit.core.data.DeviceInfo;
import com.fixit.core.data.VersionInfo;
import com.fixit.core.utils.FILog;
import com.google.gson.Gson;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Kostyantin on 12/19/2016.
 */

public class AppConfig {

    private final static String LOG_TAG = "#CONFIGURATIONS";

    public final static String KEY_RETROFIT_C_TO = "retrofit_connect_timeout_seconds";
    public final static String KEY_RETROFIT_R_TO = "retrofit_read_timeout_seconds";
    public final static String KEY_RETROFIT_W_TO = "retrofit_write_timeout_seconds";
    public final static String KEY_SERVER_API_BASE_URL = "server_api_base_url";
    public final static String KEY_DB_VERSION = "db_version";
    public final static String KEY_COLOR_ACCENT = "colorAccent";
    public final static String KEY_SERVER_CONNECTION_RETRY_LIMIT = "server_connection_retry_limit";
    public final static String KEY_SERVER_CONNECTION_RETRY_INTERVAL_MS = "server_connection_retry_interval_ms";
    public final static String KEY_SEARCH_RESULT_POLLING_RETRY_LIMIT = "search_result_polling_retry_limit";
    public final static String KEY_SEARCH_RESULT_POLLING_RETRY_INTERVAL_MS = "search_result_polling_retry_interval_ms";
    public final static String KEY_SYNCHRONIZATION_MIN_INTERVAL_MS = "synchronization_min_interval_ms";
    public final static String KEY_MAX_TRADESMEN_SELECTION = "max_tradesmen_selection";
    public final static String KEY_ERROR_DEFAULT_DISPLAY_MSG = "error_default_display_msg";
    public final static String KEY_ERROR_NO_NETWORK_MSG = "error_no_network_msg";
    public final static String KEY_API_KEY = "api_key";
    public final static String KEY_USER_AGENT = "user_agent";
    public final static String KEY_EMAIL_FOR_SUPPORT = "email_for_support";

    private final static String KEY_DEVICE_INFO = "device_info";
    private final static String KEY_APP_INFO ="app_info";

    private final static ConcurrentMap<String, Object> configurations = new ConcurrentHashMap<>();

    private final static Gson mGson = new Gson();

    public static int getColor(Context context, String code, int defaultColor) {
        Object config = configurations.get(code);
        if(config == null) {
            Resources resources = context.getResources();
            int resId = resources.getIdentifier(code, "color", context.getPackageName());
            if(resId > 0) {
                int color = resources.getColor(resId);
                configurations.putIfAbsent(code, color);
                return color;
            }
        } else {
            return (int) config;
        }
        return defaultColor;
    }

    public static Integer getInt(Context context, String code, Integer defaultVal) {
        Object config = configurations.get(code);
        if(config == null) {
            Resources resources = context.getResources();
            int resId = resources.getIdentifier(code, "integer", context.getPackageName());
            if(resId > 0) {
                Integer val = resources.getInteger(resId);
                configurations.putIfAbsent(code, val);
                return val;
            }
        } else {
            return (Integer) config;
        }
        return defaultVal;
    }

    public static Boolean getBoolean(Context context, String code, Boolean defaultVal) {
        Object config = configurations.get(code);
        if(config == null) {
            Resources resources = context.getResources();
            int resId = resources.getIdentifier(code, "bool", context.getPackageName());
            if(resId > 0) {
                Boolean val = resources.getBoolean(resId);
                configurations.putIfAbsent(code, val);
                return val;
            }
        } else {
            return (Boolean) config;
        }
        return defaultVal;
    }

    public static String getString(Context context, String code, String defaultVal) {
        Object config = configurations.get(code);
        if(config == null) {
            Resources resources = context.getResources();
            int resId = resources.getIdentifier(code, "string", context.getPackageName());
            if(resId > 0) {
                String val = resources.getString(resId);
                configurations.putIfAbsent(code, val);
                return val;
            }
        } else {
            return (String) config;
        }
        return defaultVal;
    }

    public static VersionInfo getVersionInfo(Context context) {
        VersionInfo versionInfo;
        String applicationInformation = (String) configurations.get(KEY_APP_INFO);
        if(applicationInformation == null) {

            PackageInfo pInfo = null;
            if(context != null) {
                try {
                    pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    FILog.e(LOG_TAG, "Could get version name from package manager");
                }
            }

            String versionName;
            int versionCode;
            if(pInfo != null) {
                versionName = pInfo.versionName;
                versionCode = pInfo.versionCode;
            } else {
                versionName = "";
                versionCode = 0;
            }

            versionInfo = new VersionInfo(versionName, versionCode);
            applicationInformation = mGson.toJson(versionInfo);

            configurations.putIfAbsent(KEY_APP_INFO, applicationInformation);
        } else {
            versionInfo = mGson.fromJson(applicationInformation, VersionInfo.class);
        }
        return versionInfo;
    }

    public static DeviceInfo getDeviceInfo() {
        DeviceInfo deviceInfo;
        String deviceInformation = (String) configurations.get(KEY_DEVICE_INFO);
        if(deviceInformation == null) {
            deviceInfo = new DeviceInfo(
                    Build.BRAND,
                    Build.MODEL,
                    Build.MANUFACTURER,
                    Build.DEVICE,
                    new VersionInfo(
                            Build.VERSION.RELEASE,
                            Build.VERSION.SDK_INT
                    )
            );
            deviceInformation = mGson.toJson(deviceInfo);

            configurations.putIfAbsent(KEY_DEVICE_INFO, deviceInformation);
        } else {
            deviceInfo = mGson.fromJson(deviceInformation, DeviceInfo.class);
        }

        return deviceInfo;
    }

}
