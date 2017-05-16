package com.fixit.app.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fixit.app.ui.fragments.OrderDetailsFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.ui.activities.BaseActivity;
import com.fixit.core.utils.Constants;

import java.util.ArrayList;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderActivity extends BaseActivity<OrderController>
    implements OrderDetailsFragment.OrderDetailsInteractionListener {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fixit.core.R.layout.layout_fragment_holder);

        if(savedInstanceState == null) {
            ArrayList<TradesmanWrapper> tradesmen = getIntent().getExtras().getParcelableArrayList(Constants.ARG_TRADESMEN);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(com.fixit.core.R.id.fragment_holder, OrderDetailsFragment.newInstance(tradesmen))
                    .commit();
        }
    }

    @Override
    public OrderController createController() {
        return new OrderController((BaseApplication) getApplication());
    }

    @Override
    public void showOrderReasons() {

    }
}
