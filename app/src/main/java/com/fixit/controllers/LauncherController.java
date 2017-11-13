package com.fixit.controllers;

import com.fixit.FixxitApplication;
import com.fixit.general.AppInitializationTask;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Kostyantin on 4/1/2017.
 */

public class LauncherController extends BaseController implements AppInitializationTask.AppInitializationCallback {

    private boolean isInitializing = false;
    private boolean isInitialized = false;

    private AppInitializationTask initializationTask;

    public LauncherController(FixxitApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);
    }

    public void initializeApp() {
        if(!isInitializing && !isInitialized) {
            isInitializing = true;
            initializationTask = new AppInitializationTask(getApplicationContext(), this);
            initializationTask.start();
        } else if(isInitialized){
            onInitializationComplete(Collections.<String>emptySet());
        }
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

    public void stopInitializationTask() {
        if(initializationTask != null) {
            initializationTask.stopTask();
        }
    }

    @Override
    public void updateInstallationId(String installationId) {
        getServerApiFactory().updateAppInstallationId(installationId);
    }

}
