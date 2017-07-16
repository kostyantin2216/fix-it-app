package com.fixit.core.controllers;

import com.fixit.core.factories.DAOFactory;
import com.fixit.core.factories.APIFactory;
import com.fixit.core.ui.fragments.ErrorFragment;

/**
 * Created by Kostyantin on 3/12/2017.
 */

public interface ActivityController {
    public UiCallback getUiCallback();
    public DAOFactory getDaoFactory();
    public APIFactory getServerApiFactory();

    public interface UiCallback {
        void showError(ErrorFragment.ErrorType errorType);
    }
}
