package com.fixit.exceptions;

import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Kostyantin on 9/18/2017.
 */

public class CrashlyticsException extends Exception {

    public CrashlyticsException(String message) {
        super(message);
    }

    public CrashlyticsException(String message, Throwable error) {
        super(message, error);
    }
}
