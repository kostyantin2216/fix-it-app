package com.fixit.core.utils;

import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.fixit.core.config.AppConfig;
import com.fixit.core.data.ServerLog;
import com.fixit.core.rest.apis.ServerLogDataAPI;
import com.fixit.core.rest.callbacks.EmptyCallback;

import java.util.Date;

/**
 * Created by Kostyantin on 12/19/2016.
 */

public class FILog {

    private final static String LOG_PREFIX = "FILog - ";

    private enum Level {
        INFO,
        WARN,
        ERROR
    }


    public static void i(String message) {
        log(Level.INFO, null, message, null, null, null);
    }

    public static void i(String tag, String message) {
        log(Level.INFO, tag, message, null, null, null);
    }

    public static void i(String tag, String message, ServerLogDataAPI api, String userId) {
        log(Level.INFO, tag, message, new Throwable(message), api, userId);
    }

    public static void w(String message) {
        log(Level.WARN, null, message, null, null, null);
    }

    public static void w(String tag, String message) {
        log(Level.WARN, tag, message, null, null, null);
    }


    public static void e(String message) {
        log(Level.ERROR, null, message, new Throwable(message) ,null, null);
    }

    public static void e(String tag, String message) {
        log(Level.ERROR, tag, message, new Throwable(message), null, null);
    }

    public static void e(String tag, String message, Throwable cause) {
        log(Level.ERROR, tag, message, cause, null, null);
    }

    public static void e(String tag, String message, Throwable cause, ServerLogDataAPI api, String userId) {
        log(Level.ERROR, tag, message, cause, api, userId);
    }


    private static void log(Level level, String tag, String message, Throwable cause, ServerLogDataAPI logApi, String userId) {
        if(tag == null) {
            tag = "";
        }
        tag = LOG_PREFIX + tag;

        switch (level) {
            case INFO:
                Log.i(tag, message, cause);
                break;
            case ERROR:
                Log.e(tag, message, cause);
                break;
        }

        if(logApi != null) {
            ServerLog serverLog = new ServerLog(
                    userId,
                    level.name(),
                    tag,
                    message, Log.getStackTraceString(cause),
                    AppConfig.getDeviceInfo(),
                    AppConfig.getVersionInfo(null),
                    new Date()
            );

            logApi.create(serverLog).enqueue(new EmptyCallback<ServerLog>());
        }
    }

}
