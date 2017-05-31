package com.fixit.app.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.OrderCompletionFragment;
import com.fixit.app.ui.fragments.OrderDetailsFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.utils.Constants;

import java.util.ArrayList;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderActivity extends BaseAppActivity<OrderController>
    implements OrderDetailsFragment.OrderDetailsInteractionListener, OrderController.TradesmenOrderCallback {

    private final static String FRAG_TAG_ORDER_COMPLETE = "orderCompleteFrag";

    private JobLocation mJobLocation;
    private Tradesman[] mTradesmen;

    private boolean orderSent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(com.fixit.core.R.layout.layout_fragment_holder);

        Bundle intentExtras = getIntent().getExtras();
        mJobLocation = intentExtras.getParcelable(Constants.ARG_JOB_LOCATION);
        if(savedInstanceState == null) {
            ArrayList<TradesmanWrapper> tradesmen = intentExtras.getParcelableArrayList(Constants.ARG_TRADESMEN);
            mTradesmen = TradesmanWrapper.unwrap(tradesmen);
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

    @Override
    public void completeOrder(String reason) {
        orderSent = true;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_holder, OrderCompletionFragment.newInstance(), FRAG_TAG_ORDER_COMPLETE)
                .addToBackStack(null)
                .commit();

        getController().orderTradesmen(mTradesmen, mJobLocation, reason, this);
    }

    @Override
    public void onOrderComplete(boolean success) {
        OrderCompletionFragment fragment = (OrderCompletionFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_ORDER_COMPLETE);
        if(fragment != null) {
            fragment.onOrderComplete();
        }
    }
}
