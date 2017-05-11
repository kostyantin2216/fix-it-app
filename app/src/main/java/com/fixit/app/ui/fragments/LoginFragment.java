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
 * Created by konstantin on 5/8/2017.
 */

public class LoginFragment extends BaseFragment<UserController>
    implements View.OnClickListener {


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);



        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
