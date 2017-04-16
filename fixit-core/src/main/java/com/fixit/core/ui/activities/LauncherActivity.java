package com.fixit.core.ui.activities;

import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.LauncherController;
import com.fixit.core.general.AppInitializationTask;
import com.fixit.core.utils.FILog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Kostyantin on 4/1/2017.
 */

public abstract class LauncherActivity extends BaseActivity<LauncherController> {

    private final static String LOG_TAG = "#" + LauncherActivity.class.getSimpleName();

    @Override
    public LauncherController createController() {
        return new LauncherController((BaseApplication) getApplication());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        getController().initializeApp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getController().isInitialized()) {
            onAppReady();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onInitializationComplete(AppInitializationTask.InitializationCompleteEvent completeEvent) {
        onAppReady();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onInitializationError(AppInitializationTask.InitializationErrorEvent errorEvent) {
        if(errorEvent.isFatal) {
            FILog.e(LOG_TAG, "FATAL ERROR during app initialization: " + errorEvent.error, null, getApplicationContext());
        }
    }

    public abstract void onAppReady();

}
