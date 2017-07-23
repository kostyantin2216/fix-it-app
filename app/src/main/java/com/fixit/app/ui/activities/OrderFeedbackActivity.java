package com.fixit.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fixit.app.R;
import com.fixit.app.ifs.OrderFeedbackFlowManager;
import com.fixit.app.ui.fragments.TradesmanReviewFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.controllers.ReviewController;
import com.fixit.core.data.Order;
import com.fixit.app.ui.fragments.SingleChoiceSelectionFragment;
import com.fixit.core.data.Review;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.FILog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by konstantin on 7/19/2017.
 */

public class OrderFeedbackActivity extends BaseAppActivity<OrderController>
        implements SingleChoiceSelectionFragment.SingleChoiceSelectionListener,
                   OrderFeedbackFlowManager.OrderFeedbackView,
                   TradesmanReviewFragment.TradesmanReviewListener {


    private final static int SELECTION_CODE_CONTACTED = 1;

    private Order mOrder;
    private OrderFeedbackFlowManager mFlowManager;

    @Override
    public OrderController createController() {
        return new OrderController((BaseApplication) getApplication(), this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fixit.core.R.layout.layout_fragment_holder);

        Intent intent = getIntent();
        long orderId = intent.getLongExtra(Constants.ARG_ORDER_ID, -1);

        mOrder = getController().getOrder(orderId);
        if (mOrder == null) {
            FILog.e(OrderFeedbackActivity.class.getName(), "Could not find order with id " + orderId + " for feedback flow.", this);
            startActivity(new Intent(this, SearchActivity.class));
            finishAffinity();
        } else {
            boolean fromAction = intent.getBooleanExtra(Constants.ARG_FROM_ACTION, false);
            boolean yesAction = intent.getBooleanExtra(Constants.ARG_SELECTED_YES, false);
            mFlowManager = new OrderFeedbackFlowManager(mOrder, this, fromAction, yesAction);
        }
    }

    @Override
    public void transitionTo(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(com.fixit.core.R.anim.enter_from_right, com.fixit.core.R.anim.exit_out_left)
                .replace(com.fixit.core.R.id.fragment_holder, fragment)
                .commit();
    }

    @Override
    public SingleChoiceSelectionFragment.SelectionBuilder getSelections(int selectionCode) {
        return mFlowManager.getSelections(selectionCode);
    }

    @Override
    public void onSelectionMade(int selectionCode, Object selection) {
        mFlowManager.onSelectionMade(selectionCode, selection);
    }

    @Override
    public void closeApp() {
        finishAffinity();
    }

    @Override
    public void onTradesmanReviewed(boolean isNewReview, Review review) {
        ReviewController controller = getController();
        if(isNewReview) {
            controller.saveReview(review);
        } else {
            controller.updateReview(review);
        }
        mFlowManager.tradesmanReviewed();
    }
}
