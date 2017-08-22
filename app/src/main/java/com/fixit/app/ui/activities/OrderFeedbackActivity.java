package com.fixit.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fixit.app.R;
import com.fixit.app.ifs.feedback.BaseOrderFeedbackFlow;
import com.fixit.app.ifs.feedback.ChoiceSelection;
import com.fixit.app.ifs.feedback.OrderFeedbackFlowManager;
import com.fixit.app.ifs.feedback.OrderFeedbackView;
import com.fixit.app.ui.fragments.TradesmanReviewFragment;
import com.fixit.app.ui.fragments.feedback.ChoiceSelectionFragment;
import com.fixit.app.ui.fragments.feedback.MultiChoiceSelectionFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.Order;
import com.fixit.app.ui.fragments.feedback.SingleChoiceSelectionFragment;
import com.fixit.core.data.Review;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.FILog;

import java.util.List;

/**
 * Created by konstantin on 7/19/2017.
 */

public class OrderFeedbackActivity extends BaseAppActivity<OrderController>
        implements ChoiceSelectionFragment.ChoiceSelectionListener,
                   OrderFeedbackView,
                   TradesmanReviewFragment.TradesmanReviewListener {

    private Order mOrder;
    private BaseOrderFeedbackFlow mFeedbackFlow;

    @Override
    public OrderController createController() {
        return new OrderController((BaseApplication) getApplication(), this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        long orderId = intent.getLongExtra(Constants.ARG_ORDER_ID, -1);
        int flowCode = intent.getIntExtra(Constants.ARG_FLOW_CODE, -1);

        if(flowCode > -1) {
            mOrder = getController().getOrder(orderId);
            if (mOrder == null) {
                FILog.e(OrderFeedbackActivity.class.getName(), "Could not find order with id " + orderId + " for feedback flow.", this);
                startActivity(new Intent(this, SearchActivity.class));
                finishAffinity();
            } else {
                boolean fromAction = intent.getBooleanExtra(Constants.ARG_FROM_ACTION, false);
                boolean actionYes = intent.getBooleanExtra(Constants.ARG_SELECTED_YES, false);
                mFeedbackFlow = OrderFeedbackFlowManager.createFlow(flowCode, mOrder, this, fromAction, actionYes);
            }
        } else {
            FILog.e("Cannot start feedback flow without a flow code.", this);
        }
    }

    @Override
    public void transitionTo(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(com.fixit.core.R.anim.enter_from_right, com.fixit.core.R.anim.exit_out_left)
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
}
