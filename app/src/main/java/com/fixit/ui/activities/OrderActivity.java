package com.fixit.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;

import com.fixit.app.R;
import com.fixit.FixItApplication;
import com.fixit.controllers.OrderController;
import com.fixit.data.JobLocation;
import com.fixit.data.JobReason;
import com.fixit.data.Order;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.data.Tradesman;
import com.fixit.data.TradesmanWrapper;
import com.fixit.utils.Constants;
import com.fixit.notifications.OrderNotificationManager;
import com.fixit.ui.fragments.JobReasonsSelectionFragment;
import com.fixit.ui.fragments.OrderCompletionFragment;
import com.fixit.ui.fragments.OrderDetailsFragment;

import java.util.ArrayList;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderActivity extends BaseActivity<OrderController>
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
    private String comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(com.fixit.app.R.layout.layout_fragment_holder);

        Bundle intentExtras = getIntent().getExtras();
        mJobLocation = intentExtras.getParcelable(Constants.ARG_JOB_LOCATION);
        mProfession = intentExtras.getParcelable(Constants.ARG_PROFESSION);
        if(savedInstanceState == null) {
            ArrayList<TradesmanWrapper> tradesmen = intentExtras.getParcelableArrayList(Constants.ARG_TRADESMEN);
            mTradesmen = TradesmanWrapper.unwrap(tradesmen);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left)
                    .add(com.fixit.app.R.id.fragment_holder, OrderDetailsFragment.newInstance(tradesmen), FRAG_TAG_ORDER_DETAILS)
                    .commit();
        }
    }

    @Override
    public OrderController createController() {
        return new OrderController((FixItApplication) getApplication(), this);
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
            fragment.onJobReasonSelectionChanged(reasons.length);
        }
    }

    @Override
    public void completeOrder(String comment) {
        if(isUserRegistered()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left)
                    .add(R.id.fragment_holder, OrderCompletionFragment.newInstance(), FRAG_TAG_ORDER_COMPLETE)
                    .addToBackStack(null)
                    .commit();

            this.comment = comment;
            getController().orderTradesmen(mProfession.getId(), mTradesmen, mJobLocation, mJobReasons, comment, this);
            getAnalyticsManager().trackTradesmanOrder(mProfession.getName(), mTradesmen.length);
            setToolbarTitle(getString(R.string.order_complete));
        } else{
            requestLogin(getString(R.string.login_for_order), getString(R.string.question_exit_without_login), null, this);
        }
    }

    @Override
    public void onOrderComplete(OrderData orderData) {
        OrderCompletionFragment fragment = (OrderCompletionFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_ORDER_COMPLETE);
        if(fragment != null) {
            fragment.onOrderComplete();
        }
        Order order = getController().orderCompleted(
                orderData.get_id(),
                mJobLocation,
                mProfession,
                mTradesmen,
                mJobReasons,
                orderData.getComment(),
                orderData.getCreatedAt()
        );
        OrderNotificationManager.initiateOrderFeedback(this, order.getId());
        getAnalyticsManager().trackOrderConfirmed(
                mProfession.getName(),
                mTradesmen.length,
                mJobReasons.length,
                !TextUtils.isEmpty(comment)
        );
    }

    @Override
    public void loginComplete(boolean success, @Nullable Bundle data) {
        if(success) {
            OrderDetailsFragment orderDetailsFragment = getFragment(FRAG_TAG_ORDER_DETAILS, OrderDetailsFragment.class);
            String comment;
            if(orderDetailsFragment != null) {
                comment = orderDetailsFragment.getComment();
            } else {
                comment = JobReason.toDescription(mJobReasons);
            }
            completeOrder(comment);
        } else {
            showPrompt(getString(R.string.cannot_continue_without_login));
        }
    }

    @Override
    public JobReason[] getSelectedJobReasons() {
        return mJobReasons;
    }

}
