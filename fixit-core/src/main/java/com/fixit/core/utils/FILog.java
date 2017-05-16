package com.fixit.core.utils;

import android.content.Context;
import android.util.Log;

import com.fixit.core.general.ErrorReporter;

import java.io.IOException;

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

    public static void e(String message, Throwable t) {
        log(Level.ERROR, null, message, t , null);
    }

    public static void e(String tag, String message) {
        log(Level.ERROR, tag, message, new Throwable(message), null);
    }

    public static void e(String tag, String message, Throwable cause) {
        log(Level.ERROR, tag, message, cause, null);
    }

    public static void e(String tag, String message, Throwable cause, Context context) {
        log(Level.ERROR, tag, message, cause, context);
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
            case ERROR:
                Log.e(tag, message, cause);
                break;
        }

        if(context != null) {
            ErrorReporter.report(context, level.name(), tag, message, Log.getStackTraceString(cause));
        }
    }

}
