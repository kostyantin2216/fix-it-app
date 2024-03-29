package com.fixit.utils;

import android.content.Context;
import android.util.Log;

import com.fixit.general.ErrorReporter;

/**
 * Created by Kostyantin on 12/19/2016.
 */

public class FILog {

    private final static String LOG_PREFIX = "FILog - ";

    private enum Level {
        INFO,
        WARN,
        ERROR,
        DEBUG,
        WHAT_A_TERRIBLE_FAILURE
    }


    public static void i(String message) {
        log(Level.INFO, null, message, null, null);
    }

    public static void i(String tag, String message) {
        log(Level.INFO, tag, message, null, null);
    }

    public static void i(String tag, String message, Context context) {
        log(Level.INFO, tag, message, new Throwable(message), context);
    }

    public static void w(String message) {
        log(Level.WARN, null, message, null, null);
    }

    public static void w(String message, Context context) {
        log(Level.WARN, null, message, null, context);
    }

    public static void w(String tag, String message) {
        log(Level.WARN, tag, message, null, null);
    }

    public static void w(String tag, String message, Context context) {
        log(Level.WARN, tag, message, null, context);
    }

    public static void e(String message, Context context) {
        log(Level.ERROR, null, message, null, context);
    }

    public static void e(String message, Throwable t) {
        log(Level.ERROR, null, message, t , null);
    }

    public static void e(String tag, String message) {
        log(Level.ERROR, tag, message, new Throwable(message), null);
    }

    public static void e(String tag, String message, Context context) {
        log(Level.ERROR, tag, message, new Throwable(message), context);
    }

    public static void e(String tag, String message, Throwable cause) {
        log(Level.ERROR, tag, message, cause, null);
    }

    public static void e(String tag, String message, Throwable cause, Context context) {
        log(Level.ERROR, tag, message, cause, context);
    }

    public static void wtf(String tag, String message, Throwable cause) {
        log(Level.WHAT_A_TERRIBLE_FAILURE, tag, message, cause, null);
    }

    public static void wtf(String tag, String message) {
        log(Level.WHAT_A_TERRIBLE_FAILURE, tag, message, null, null);
    }

    public static void d(String tag, String message) {
        log(Level.DEBUG, tag, message, null, null);
    }

    private static void log(Level level, String tag, String message, Throwable cause, Context context) {
        if(tag == null) {
            tag = "";
        }
        tag = LOG_PREFIX + tag;

        switch (level) {
            case INFO:
                Log.i(tag, message, cause);
                break;
            case WARN:
                Log.w(tag, message, cause);
                break;
            case ERROR:
                Log.e(tag, message, cause);
                break;
            case WHAT_A_TERRIBLE_FAILURE:
                Log.wtf(tag, message);
                break;
        }

        if(context != null) {
            ErrorReporter.report(context, level.name(), tag, message, Log.getStackTraceString(cause));
        }
    }

}
