package com.fixit.core.general;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;

import com.fixit.core.config.AppConfig;
import com.fixit.core.data.AppInstallation;
import com.fixit.core.factories.APIFactory;
import com.fixit.core.factories.DAOFactory;
import com.fixit.core.rest.apis.AppInstallationAPI;
import com.fixit.core.rest.apis.SynchronizationServiceAPI;
import com.fixit.core.synchronization.SynchronizationTask;
import com.fixit.core.utils.FILog;
import com.fixit.core.utils.PrefUtils;

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

    private int mAsyncCount;
    private boolean finishedSynchronousWork = false;

    public AppInitializationTask(Context context, AppInitializationCallback callback) {
        mContextReference = new WeakReference<>(context);
        mCallback = callback;
        mErrors = new HashSet<>();
        mAsyncCount = 0;
        mHandler = new Handler(context.getMainLooper());
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

            if(TextUtils.isEmpty(PrefUtils.getInstallationId(context))) {
                sendInstallation(context, serverAPIFactory.createAppInstallationApi());
            }

            synchronizeDatabase(context, serverAPIFactory.createSynchronizationApi(), daoFactory);
        } else {
            FILog.e(LOG_TAG, "cannot initialize app without context");
        }

        finishedSynchronousWork = true;
        if (isComplete()) {
            finish();
        }
    }

    private void sendInstallation(Context context, AppInstallationAPI api) {
        AppInstallation appInstallation = new AppInstallation(
                "", // TODO: get url from google play.
                AppConfig.getDeviceInfo(context),
                AppConfig.getVersionInfo(context),
                new Date()
        );

        try {
            Response<AppInstallation> response = api.create(appInstallation).execute();
            if(response != null) {
                appInstallation = response.body();
                PrefUtils.setInstallationId(context, appInstallation.getId());
            }
        } catch (IOException e) {
            // Do nothing, try again next time app opens.
            FILog.e(LOG_TAG, "Couldn't send app installation to server.", e);
        }
    }

    private void synchronizeDatabase(Context context, SynchronizationServiceAPI api, DAOFactory daoFactory) {
        SynchronizationTask task = new SynchronizationTask(context, api, daoFactory, new SynchronizationTask.SynchronizationCallback() {
            @Override
            public void onSynchronizationComplete() {
                finishAsync();
            }

            @Override
            public void onSynchronizationError(String msg, Throwable t) {
                mCallback.onInitializationError(msg, true);
                finishAsync();
            }
        });

        if(task.isReadyForSynchronization()) {
            startAsync();
            task.run(); // We are already in a background thread so just run the sync task thread instead of starting it.
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
        return mAsyncCount == 0 && finishedSynchronousWork;
    }

    private void finish() {
        FILog.i(LOG_TAG, "finished app synchronization");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onInitializationComplete(mErrors);
                mErrors.clear();
            }
        });
    }

    public interface AppInitializationCallback {
        Context getApplicationContext();
        APIFactory getServerApiFactory();
        DAOFactory getDaoFactory();
        void onInitializationComplete(Set<String> errors);
        void onInitializationError(String error, boolean fatal);
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
