package com.fixit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fixit.FixxitApplication;
import com.fixit.app.R;
import com.fixit.controllers.OrderController;
import com.fixit.controllers.SearchController;
import com.fixit.data.JobLocation;
import com.fixit.data.Order;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.data.Review;
import com.fixit.data.Tradesman;
import com.fixit.external.google.GoogleClientManager;
import com.fixit.rest.APIError;
import com.fixit.ui.components.OngoingOrderView;
import com.fixit.ui.fragments.AboutFragment;
import com.fixit.ui.fragments.TradesmanReviewFragment;
import com.fixit.ui.helpers.OrderedTradesmanInteractionHandler;
import com.fixit.ui.helpers.TradesmanActionHandler;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.fixit.utils.GlobalPreferences;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.List;

/**
 * Created by Kostyantin on 10/28/2017.
 */

public class SearchActivity extends BaseActivity<SearchController>
        implements SearchController.SearchCallback,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleClientManager.GoogleManagerCallback,
        OrderedTradesmanInteractionHandler.OrderedTradesmanInteractionListener,
        TradesmanReviewFragment.TradesmanReviewListener {

    public static final int DELEGATE_ORDER_HISTORY = 0;

    private GoogleClientManager mGoogleClientManager;

    private ViewGroup mRoot;

    private boolean navInitialized = false;

    public GoogleClientManager getGoogleClientManager() {
        return mGoogleClientManager;
    }

    public ViewGroup getRootView() {
        return mRoot;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean delegating = checkActivityDelegate();
        if(!delegating) {
            setContentView(R.layout.activity_drawer_layout);

            mRoot = (ViewGroup) findViewById(R.id.fragment_holder);

            mGoogleClientManager = new GoogleClientManager(new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API),
                    this,
                    this
            );
        }
    }

    private boolean checkActivityDelegate() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.ARG_DELEGATE)) {
            switch (extras.getInt(Constants.ARG_DELEGATE)) {
                case DELEGATE_ORDER_HISTORY:
                    startActivity(new Intent(this, OrderHistoryActivity.class));
                    return true;
            }
        }
        return false;
    }

    @Override
    public void setToolbar(Toolbar toolbar, boolean homeAsUpEnabled) {
        super.setToolbar(toolbar, homeAsUpEnabled);

        if (!navInitialized) {
            initNavigationDrawer(toolbar);
            navInitialized = true;
        }
    }

    protected void initNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard(mRoot.getRootView().getWindowToken());
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        invalidateNavigationHeader(navigationView);
    }

    private void invalidateNavigationHeader(NavigationView navigationView) {
        if (!TextUtils.isEmpty(GlobalPreferences.getUserId(this))) {
            MenuItem loginItem = navigationView.getMenu().findItem(R.id.login);
            loginItem.setVisible(false);
        }

        SearchController controller = getController();
        OrderData order = controller.getLatestOrder();
        View headerView = navigationView.getHeaderView(0);
        if (order != null && !order.isFeedbackProvided()) {
            final OngoingOrderView ongoingOrderView = (OngoingOrderView) headerView.findViewById(R.id.ongoing_order_view);
            String ongoingOrderId = ongoingOrderView.getOrderId();

            if (TextUtils.isEmpty(ongoingOrderId) || !ongoingOrderId.equals(order.get_id())) {
                controller.loadLatestOrder(new OrderController.SingleOrderCallback() {
                    @Override
                    public void onOrderLoaded(Order order) {
                        ongoingOrderView.setOrder(order);
                        if (ongoingOrderView.getInteractionHandler() == null) {
                            ongoingOrderView.setInteractionHandler(new OrderedTradesmanInteractionHandler(SearchActivity.this));
                        }
                    }

                    @Override
                    public void onUnexpectedErrorOccurred(String msg, Throwable t) {
                        SearchActivity.this.onUnexpectedErrorOccurred(msg, t);
                    }

                    @Override
                    public void onAppServiceError(List<APIError> errors) {
                        SearchActivity.this.onAppServiceError(errors);
                    }

                    @Override
                    public void onServerError() {
                        SearchActivity.this.onServerError();
                    }
                });
            }
        } else {
            headerView.setVisibility(View.GONE);
        }
    }

    private void closeNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (navInitialized) {
            invalidateNavigationHeader((NavigationView) findViewById(R.id.nav_view));
        } else {
            FILog.i(Constants.LOG_TAG_SEARCH, "nav not initialized", this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (navInitialized) {
            getController().cleanOrderFactory();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleClientManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public SearchController createController() {
        return new SearchController((FixxitApplication) getApplication(), this);
    }

    @Override
    public void invalidAddress() {
        notifyUser(getString(R.string.invalid_address));
        hideLoader();
    }

    @Override
    public void unsupportedAddress() {
        notifyUser(getString(R.string.unsupported_address));
        hideLoader();
    }

    @Override
    public void invalidProfession() {
        notifyUser(getString(R.string.invalid_profession));
        hideLoader();
    }

    @Override
    public void onAddressValidated() {
        showLoader(getString(R.string.starting_search));
    }

    @Override
    public void onSearchStarted(Profession profession, JobLocation location, String searchId) {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(Constants.ARG_SEARCH_ID, searchId);
        intent.putExtra(Constants.ARG_JOB_LOCATION, location);
        intent.putExtra(Constants.ARG_PROFESSION, profession);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        hideLoader();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeNavigationDrawer();
        switch (item.getItemId()) {
            case R.id.login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.order_history:
                startActivity(new Intent(this, OrderHistoryActivity.class));
                return true;
            case R.id.about:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                        .add(R.id.fragment_holder, AboutFragment.newInstance())
                        .commit();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onOrderViewInteraction() {
        closeNavigationDrawer();
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

    @Override
    public void showDialogFragment(DialogFragment dialogFragment, String tag) {
        dialogFragment.show(getSupportFragmentManager(), tag);
    }

    @Override
    public Activity getActivityForResult() {
        return this;
    }

}

