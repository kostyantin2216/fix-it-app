package com.fixit.app.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;

import com.fixit.app.R;
import com.fixit.app.ifs.notifications.OrderNotificationManager;
import com.fixit.app.ui.fragments.JobReasonsSelectionFragment;
import com.fixit.app.ui.fragments.OrderCompletionFragment;
import com.fixit.app.ui.fragments.OrderDetailsFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.JobReason;
import com.fixit.core.data.Order;
import com.fixit.core.data.Profession;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.ui.activities.BaseActivity;
import com.fixit.core.utils.Constants;

import java.util.ArrayList;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderActivity extends BaseAppActivity<OrderController>
    implements OrderDetailsFragment.OrderDetailsInteractionListener,
               OrderController.TradesmenOrderCallback,
               JobReasonsSelectionFragment.JobReasonsInteractionListener,
               BaseActivity.LoginRequester {

    private final static String FRAG_TAG_ORDER_DETAILS = "orderDetailsFrag";
    private final static String FRAG_TAG_ORDER_COMPLETE = "orderCompleteFrag";

    private JobLocation mJobLocation;
    private Profession mProfession;
    private Tradesman[] mTradesmen;
    private JobReason[] mJobReasons = new JobReason[0];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(com.fixit.core.R.layout.layout_fragment_holder);

        Bundle intentExtras = getIntent().getExtras();
        mJobLocation = intentExtras.getParcelable(Constants.ARG_JOB_LOCATION);
        mProfession = intentExtras.getParcelable(Constants.ARG_PROFESSION);
        if(savedInstanceState == null) {
            ArrayList<TradesmanWrapper> tradesmen = intentExtras.getParcelableArrayList(Constants.ARG_TRADESMEN);
            mTradesmen = TradesmanWrapper.unwrap(tradesmen);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left)
                    .add(com.fixit.core.R.id.fragment_holder, OrderDetailsFragment.newInstance(tradesmen), FRAG_TAG_ORDER_DETAILS)
                    .commit();
        }
    }

    @Override
    public OrderController createController() {
        return new OrderController((BaseApplication) getApplication(), this);
    }

    @Override
    public void showOrderReasons() {
        getAnalyticsManager().trackShowReasons();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left)
                .add(R.id.fragment_holder, JobReasonsSelectionFragment.newInstance(mProfession.getId()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onJobReasonsSelected(JobReason[] reasons) {
        mJobReasons = reasons;
        getSupportFragmentManager().popBackStack();
        OrderDetailsFragment fragment = (OrderDetailsFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_ORDER_DETAILS);
        if(fragment != null) {
            fragment.setReason(JobReason.toDescription(reasons));
        }
    }

    @Override
    public void completeOrder(String reason) {
        if(isUserRegistered()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left)
                    .add(R.id.fragment_holder, OrderCompletionFragment.newInstance(), FRAG_TAG_ORDER_COMPLETE)
                    .addToBackStack(null)
                    .commit();

            getController().orderTradesmen(mTradesmen, mJobLocation, reason, this);
            setToolbarTitle(getString(R.string.order_complete));
            getAnalyticsManager().trackJobReasonsSelected(!TextUtils.isEmpty(reason) && mJobReasons.length == 0, mJobReasons.length);
        } else{
            requestLogin(this, null);
        }
    }

    @Override
    public void onOrderComplete(boolean success) {
        OrderCompletionFragment fragment = (OrderCompletionFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_ORDER_COMPLETE);
        if(fragment != null) {
            fragment.onOrderComplete();
        }
        Order order = getController().saveOrder(mJobLocation, mProfession, mTradesmen, mJobReasons);
        OrderNotificationManager.initiateOrderFeedback(this, order);
    }

    @Override
    public void loginComplete(boolean success, @Nullable Bundle data) {
        if(success) {
            OrderDetailsFragment orderDetailsFragment = getFragment(FRAG_TAG_ORDER_DETAILS, OrderDetailsFragment.class);
            String reason;
            if(orderDetailsFragment != null) {
                reason = orderDetailsFragment.getReason();
            } else {
                reason = JobReason.toDescription(mJobReasons);
            }
            completeOrder(reason);
        } else {
            showPrompt(getString(R.string.cannot_continue_without_login));
        }
    }

    @Override
    public JobReason[] getSelectedJobReasons() {
        return mJobReasons;
    }

}
