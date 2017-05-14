package com.fixit.app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fixit.app.R;
import com.fixit.core.controllers.UserController;
import com.fixit.core.ui.fragments.BaseFragment;

/**
 * Created by konstantin on 5/14/2017.
 */

public class UserRegistrationFragment extends BaseFragment<UserController> {

    public static UserRegistrationFragment newInstance() {
        return new UserRegistrationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_registration, container, false);

        return v;
    }
}
