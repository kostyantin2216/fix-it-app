package com.fixit.app.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.fixit.app.R;
import com.fixit.app.ui.helpers.OrderedTradesmanInteractionHandler;
import com.fixit.app.ui.fragments.OrderHistoryFragment;
import com.fixit.app.ui.fragments.TradesmanProfileFragment;
import com.fixit.app.ui.fragments.TradesmanReviewFragment;
import com.fixit.app.ui.helpers.TradesmanActionHandler;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.Review;
import com.fixit.core.data.Tradesman;

/**
 * Created by konstantin on 8/8/2017.
 */

public class OrderHistoryActivity extends BaseAppActivity<OrderController>
        implements OrderedTradesmanInteractionHandler.OrderedTradesmanInteractionListener,
                   TradesmanReviewFragment.TradesmanReviewListener {

    @Override
    public OrderController createController() {
        return new OrderController((BaseApplication) getApplication(), this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_appbar_fragment_holder);

        setToolbar((Toolbar) findViewById(R.id.toolbar), true);
        setToolbarTitle(getString(R.string.order_history));

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
