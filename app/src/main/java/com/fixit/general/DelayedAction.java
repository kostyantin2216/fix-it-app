package com.fixit.general;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Kostyantin on 11/2/2017.
 */

public class DelayedAction {

    private final static long DEFAULT_DELAY_MILLIS = 700;

    private final Runnable mAction;
    private final Handler mHandler;
    private final long mDefaultDelayMillis;


    public DelayedAction(Runnable action) {
        this(action, new Handler(Looper.getMainLooper()), DEFAULT_DELAY_MILLIS);
    }

    public DelayedAction(Runnable action, Handler handler) {
        this(action, handler, DEFAULT_DELAY_MILLIS);
    }

    public DelayedAction(Runnable action, long defaultDelayMillis) {
        this(action, new Handler(Looper.getMainLooper()), defaultDelayMillis);
    }

    public DelayedAction(Runnable action, Handler handler, long defaultDelayMillis) {
        mAction = action;
        mHandler = handler;
        mDefaultDelayMillis = defaultDelayMillis;
    }

    public void perform() {
        perform(mDefaultDelayMillis);
    }

    public void perform(long delayMillis) {
        mHandler.postDelayed(mAction, delayMillis);
    }

    public void cancel() {
        mHandler.removeCallbacks(mAction);
    }

}
