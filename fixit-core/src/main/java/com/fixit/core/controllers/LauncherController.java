package com.fixit.core.controllers;

import com.fixit.core.BaseApplication;
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
        // FIXME: remove
        /*if(!isInitializing && !isInitialized) {
            isInitializing = true;
            new AppInitializationTask(getApplicationContext(), this).start();
        }*/
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void onInitializationComplete(Set<String> errors) {
        isInitializing = false;
        isInitialized = true;
        EventBus.getDefault().post(AppInitializationTask.createCompletionEvent(errors));
    }

    @Override
    public void onInitializationError(String error, boolean fatal) {
        isInitializing = false;
        EventBus.getDefault().post(AppInitializationTask.createErrorEvent(fatal, error));
    }

}
