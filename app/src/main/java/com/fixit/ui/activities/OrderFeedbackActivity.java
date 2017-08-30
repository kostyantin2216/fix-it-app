package com.fixit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.fixit.app.R;
import com.fixit.BaseApplication;
import com.fixit.controllers.OrderController;
import com.fixit.data.Order;
import com.fixit.data.Review;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.fixit.feedback.BaseOrderFeedbackFlow;
import com.fixit.feedback.ChoiceSelection;
import com.fixit.feedback.OrderFeedbackFlowManager;
import com.fixit.feedback.OrderFeedbackView;
import com.fixit.ui.fragments.TradesmanReviewFragment;
import com.fixit.ui.fragments.feedback.ChoiceSelectionFragment;

/**
 * Created by konstantin on 7/19/2017.
 */

public class OrderFeedbackActivity extends BaseAppActivity<OrderController>
        implements ChoiceSelectionFragment.ChoiceSelectionListener,
                   OrderFeedbackView,
                   TradesmanReviewFragment.TradesmanReviewListener,
                   OrderController.SingleOrderCallback {

    private Order mOrder;
    private BaseOrderFeedbackFlow mFeedbackFlow;

    private String mOrderId;
    private int mFlowCode;

    @Override
    public OrderController createController() {
        return new OrderController((BaseApplication) getApplication(), this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        mOrderId = intent.getStringExtra(Constants.ARG_ORDER_ID);
        mFlowCode = intent.getIntExtra(Constants.ARG_FLOW_CODE, -1);

        if(mFlowCode < 0) {
            abortFeedback("Cannot start flow without a flow code");
        } else if(TextUtils.isEmpty(mOrderId)) {
            abortFeedback("Cannot start flow without an order id");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mOrder == null) {
            showLoader(getString(R.string.loading));
            getController().loadOrderHistory(mOrderId, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        getController().cleanOrderFactory();
    }

    private void abortFeedback(String reason) {
        FILog.e(Constants.LOG_TAG_FEEDBACK, "Aborting! " + reason, this);
        boolean fromNotification = getIntent().getBooleanExtra(Constants.ARG_FROM_NOTIFICATION, false);

        if(fromNotification) {
            startActivity(new Intent(this, SearchActivity.class));
        }
        finish();
    }

    @Override
    public void transitionTo(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(com.fixit.app.R.anim.enter_from_right, com.fixit.app.R.anim.exit_out_left)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public ChoiceSelection.Builder getSelections(int selectionCode) {
        return mFeedbackFlow.getSelections(selectionCode);
    }

    @Override
    public void onSelectionMade(int selectionCode, Object selection) {
        mFeedbackFlow.onSelectionMade(selectionCode, selection);
    }

    @Override
    public void closeApp() {
        finishAffinity();
    }

    @Override
    public Intent createIntent(Class<?> forClass) {
        return new Intent(this, forClass);
    }

    @Override
    public void onTradesmanReviewed(boolean isNewReview, Review review) {
        mFeedbackFlow.onTradesmanReviewed();
    }

    @Override
    public void onOrderLoaded(Order order) {
        mOrder = order;
        if (mOrder == null) {
            abortFeedback("Could not find order with id " + mOrderId);
        } else {
            Intent intent = getIntent();
            boolean fromAction = intent.getBooleanExtra(Constants.ARG_FROM_ACTION, false);
            boolean actionYes = intent.getBooleanExtra(Constants.ARG_SELECTED_YES, false);
            mFeedbackFlow = OrderFeedbackFlowManager.createFlow(mFlowCode, mOrder, this, fromAction, actionYes);
            hideLoader();
        }
    }
}
