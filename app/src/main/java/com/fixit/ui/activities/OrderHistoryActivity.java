package com.fixit.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.fixit.app.R;
import com.fixit.FixxitApplication;
import com.fixit.controllers.OrderController;
import com.fixit.data.Review;
import com.fixit.data.Tradesman;
import com.fixit.ui.fragments.OrderHistoryFragment;
import com.fixit.ui.fragments.TradesmanReviewFragment;
import com.fixit.ui.helpers.OrderedTradesmanInteractionHandler;
import com.fixit.ui.helpers.TradesmanActionHandler;

/**
 * Created by konstantin on 8/8/2017.
 */

public class OrderHistoryActivity extends BaseActivity<OrderController>
        implements OrderedTradesmanInteractionHandler.OrderedTradesmanInteractionListener,
                   TradesmanReviewFragment.TradesmanReviewListener {

    @Override
    public OrderController createController() {
        return new OrderController((FixxitApplication) getApplication(), this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_appbar_fragment_holder);

        setToolbar((Toolbar) findViewById(R.id.toolbar), true);
        setToolbarTitle(getString(R.string.order_history));

        getAnalyticsManager().trackHistoryShown();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_holder, OrderHistoryFragment.newInstance())
                .commit();
    }

    @Override
    public void onOrderViewInteraction() {
        // Do nothing...
    }

    @Override
    public void showTradesman(Tradesman tradesman) {
        TradesmanActionHandler.showTradesman(getSupportFragmentManager(), tradesman);
    }

    @Override
    public void reviewTradesman(Tradesman tradesman) {
        TradesmanActionHandler.reviewTradesman(this, getSupportFragmentManager(), tradesman);
    }

    @Override
    public void onTradesmanReviewed(boolean isNewReview, Review review) {
        onBackPressed();
    }
}
