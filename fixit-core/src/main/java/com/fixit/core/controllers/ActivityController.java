package com.fixit.core.controllers;

import com.fixit.core.factories.DAOFactory;
import com.fixit.core.factories.APIFactory;

/**
 * Created by Kostyantin on 3/12/2017.
 */

public interface ActivityController {
    public DAOFactory getDaoFactory();
    public APIFactory getServerApiFactory();
}
