package com.fixit.controllers;

import com.fixit.caching.ApplicationCache;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;
import com.fixit.general.AnalyticsManager;
import com.fixit.ui.fragments.ErrorFragment;

/**
 * Created by Kostyantin on 3/12/2017.
 */

public interface ActivityController {
    UiCallback getUiCallback();
    DAOFactory getDaoFactory();
    APIFactory getServerApiFactory();
    AnalyticsManager getAnalyticsManager();
    ApplicationCache getAppCache();


    interface UiCallback {
        void showError(ErrorFragment.ErrorType errorType);
    }
}
