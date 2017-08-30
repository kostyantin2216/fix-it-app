package com.fixit.general;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;

import com.fixit.config.AppConfig;
import com.fixit.data.AppInstallation;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;
import com.fixit.rest.ServerCallback;
import com.fixit.rest.apis.AppInstallationDataAPI;
import com.fixit.rest.apis.SynchronizationServiceAPI;
import com.fixit.synchronization.SynchronizationTask;
import com.fixit.utils.FILog;
import com.fixit.utils.GlobalPreferences;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Response;

/**
 * Created by konstantin on 4/2/2017.
 */
public class AppInitializationTask extends Thread {

    private final static String LOG_TAG = AppInitializationTask.class.getSimpleName();

    private final AppInitializationCallback mCallback;
    private WeakReference<Context> mContextReference;

    private final Handler mHandler;
    private final Set<String> mErrors;

    private SynchronizationTask mSynchronizationTask;
    private int mAsyncCount;
    private boolean mFinishedSynchronousWork = false;

    private volatile boolean mStopped;

    public AppInitializationTask(Context context, AppInitializationCallback callback) {
        mContextReference = new WeakReference<>(context);
        mCallback = callback;
        mErrors = new HashSet<>();
        mAsyncCount = 0;
        mHandler = new Handler(context.getMainLooper());
    }

    public void stopTask() {
        mStopped = true;
        if(mSynchronizationTask != null) {
            mSynchronizationTask.stopTask();
        }
    }

    private Context getContext() {
        Context context = mContextReference.get();
        if(context == null && mCallback != null) {
            context = mCallback.getApplicationContext();
            mContextReference = new WeakReference<>(context);
        }
        return context;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        FILog.i(LOG_TAG, "starting app initialization");

        Context context = getContext();
        if(context != null) {
            APIFactory serverAPIFactory = mCallback.getServerApiFactory();
            DAOFactory daoFactory = mCallback.getDaoFactory();

            if(!mStopped) {
                if (TextUtils.isEmpty(GlobalPreferences.getInstallationId(context))) {
                    sendInstallation(context, serverAPIFactory.createAppInstallationApi());
                }

                if(!mStopped) {
                    synchronizeDatabase(context, serverAPIFactory.createSynchronizationApi(), daoFactory);
                }
            }
        } else {
            FILog.e(LOG_TAG, "cannot initialize app without context");
        }

        mFinishedSynchronousWork = true;
        if (isComplete()) {
            finish();
        }
    }

    private void sendInstallation(Context context, AppInstallationDataAPI api) {
        AppInstallation appInstallation = new AppInstallation(
                "", // TODO: get url from google play.
                AppConfig.getDeviceInfo(context),
                AppConfig.getVersionInfo(context),
                new Date()
        );

        try {
            Response<AppInstallation> response = api.create(appInstallation).execute();
            if(!mStopped && response != null) {
                String appInstallationId = response.body().getId();
                GlobalPreferences.setInstallationId(context, appInstallationId);
                if(!mStopped) {
                    mCallback.updateInstallationId(appInstallationId);
                }
            }
        } catch (IOException e) {
            // Do nothing, try again next time app opens.
            FILog.e(LOG_TAG, "Couldn't send app installation to server.", e);
        }
    }

    private void synchronizeDatabase(Context context, SynchronizationServiceAPI api, DAOFactory daoFactory) {
        mSynchronizationTask = new SynchronizationTask(context, api, daoFactory, new SynchronizationTask.SynchronizationCallback() {
            @Override
            public void serverUnavailable() {
                if(!mStopped) {
                    mCallback.serverUnavailable();
                }
            }

            @Override
            public void onSynchronizationComplete() {
                finishAsync();
            }

            @Override
            public void onSynchronizationError(String msg, Throwable t) {
                if(!mStopped) {
                    mCallback.onInitializationError(msg, true);
                }
                finishAsync();
            }
        });

        if(mSynchronizationTask.isReadyForSynchronization()) {
            startAsync();
            mSynchronizationTask.run(); // We are already in a background thread so just run the sync task thread instead of starting it.
        }
    }

    private void startAsync() {
        mAsyncCount++;
    }

    private synchronized void finishAsync() {
        mAsyncCount--;
        if(isComplete()) {
            finish();
        }
    }

    private boolean isComplete() {
        return mAsyncCount == 0 && mFinishedSynchronousWork;
    }

    private void finish() {
        FILog.i(LOG_TAG, "finished app synchronization");
        if(!mStopped) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onInitializationComplete(mErrors);
                    mErrors.clear();
                }
            });
        }
    }

    public interface AppInitializationCallback extends ServerCallback {
        Context getApplicationContext();
        APIFactory getServerApiFactory();
        DAOFactory getDaoFactory();
        void onInitializationComplete(Set<String> errors);
        void onInitializationError(String error, boolean fatal);
        void updateInstallationId(String installationId);
    }

    public static InitializationCompleteEvent createCompletionEvent(Set<String> errors) {
        return new InitializationCompleteEvent(errors);
    }

    public static InitializationErrorEvent createErrorEvent(boolean isFatal, String error) {
        return new InitializationErrorEvent(error, isFatal);
    }

    public static class InitializationCompleteEvent {
        public final Set<String> errors;

        private InitializationCompleteEvent(Set<String> errors) {
            this.errors = errors;
        }
    }

    public static class InitializationErrorEvent {
        public final String error;
        public final boolean isFatal;

        private InitializationErrorEvent(String error, boolean isFatal) {
            this.error = error;
            this.isFatal = isFatal;
        }
    }
}