package com.fixit.core.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.LauncherController;
import com.fixit.core.utils.FILog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Kostyantin on 4/1/2017.
 */

public abstract class LauncherActivity extends BaseActivity<LauncherController> {

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitializationComplete(LauncherController.InitializationCompleteEvent completeEvent) {
        onAppReady();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitializationError(LauncherController.InitializationErrorEvent errorEvent) {
        if(errorEvent.isFatal) {
            FILog.e("FATAL ERROR during app initialization");
        }
    }

    public abstract void onAppReady();

}
