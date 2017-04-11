package com.fixit.core.controllers;

import com.fixit.core.BaseApplication;
import com.fixit.core.factories.ServerAPIFactory;
import com.fixit.core.general.AppInitializationTask;

import org.greenrobot.eventbus.EventBus;

import java.util.Set;

/**
 * Created by Kostyantin on 4/1/2017.
 */

public class LauncherController extends BaseController implements AppInitializationTask.AppInitializationCallback {

    private boolean isInitializing = false;
    private boolean isInitialized = false;

    public LauncherController(BaseApplication baseApplication) {
        super(baseApplication);
    }

    public void initializeApp() {
        if(!isInitializing && !isInitialized) {
            isInitializing = true;
            new AppInitializationTask(getApplicationContext(), this).start();
        }
    }

    @Override
    public void onInitializationComplete(Set<String> errors) {
        isInitializing = false;
        isInitialized = true;
        EventBus.getDefault().post(new InitializationCompleteEvent(errors));
    }

    @Override
    public void onInitializationError(String error, boolean fatal) {
        isInitializing = false;
        EventBus.getDefault().post(new InitializationErrorEvent(error, fatal));
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
