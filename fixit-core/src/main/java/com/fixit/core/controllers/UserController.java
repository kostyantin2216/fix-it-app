package com.fixit.core.controllers;

import android.text.TextUtils;

import com.fixit.core.BaseApplication;
import com.fixit.core.utils.PrefUtils;

/**
 * Created by konstantin on 5/8/2017.
 */

public class UserController extends BaseController {

    public UserController(BaseApplication baseApplication) {
        super(baseApplication);
    }

    public boolean isUserRegistered() {
        return !TextUtils.isEmpty(PrefUtils.getUserId(getApplicationContext()));
    }

    public void registerUser(String firstName, String lastName, String email, String telephone, String avatarUrl) {

    }

}
