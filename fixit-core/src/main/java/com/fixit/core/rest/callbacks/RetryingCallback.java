package com.fixit.core.rest.callbacks;

import android.content.Context;

import com.fixit.core.config.AppConfig;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by konstantin on 3/30/2017.
 */

public abstract class RetryingCallback<T> implements Callback<T> {

    private final static int DEFAULT_RETRIES = 5;
    private final static int DEFAULT_RETRY_INTERVAL_MS = 800;

    private final int retryLimit;
    private final int retryIntervalMs;

    private int retries = 0;

    /* package */ volatile boolean isCanceled = false;

    public RetryingCallback(Context context) {
        this.retryLimit = AppConfig.getInt(
                context, AppConfig.KEY_SERVER_CONNECTION_RETRY_LIMIT, DEFAULT_RETRIES
        );
        this.retryIntervalMs = AppConfig.getInt(
                context, AppConfig.KEY_SERVER_CONNECTION_RETRY_INTERVAL_MS, DEFAULT_RETRY_INTERVAL_MS
        );
    }

    public RetryingCallback(int retryLimit, int retryIntervalMs) {
        this.retryLimit = retryLimit;
        this.retryIntervalMs = retryIntervalMs;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if(!isCanceled) {
            if (t instanceof IOException) { // Network error occurred.
                if (retries < retryLimit) {
                    retries++;
                    retry(call);
                } else {
                    onRetryFailure(call, t);
                }
            } else {
                onRetryFailure(call, t);
            }
        }
    }

    public abstract void onRetryFailure(Call<T> call, Throwable t);

    private void retry(final Call<T> call) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                call.clone().enqueue(RetryingCallback.this);
            }
        }, retryIntervalMs);
    }

    public void cancel() {
        this.isCanceled = true;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

}
