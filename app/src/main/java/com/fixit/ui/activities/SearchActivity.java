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

import com.fixit.app.R;
import com.fixit.BaseApplication;
import com.fixit.controllers.OrderController;
import com.fixit.controllers.SearchController;
import com.fixit.data.JobLocation;
import com.fixit.data.MutableLatLng;
import com.fixit.data.Order;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.data.Review;
import com.fixit.data.Tradesman;
import com.fixit.general.SearchManager;
import com.fixit.rest.APIError;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.fixit.external.google.GoogleClientManager;
import com.fixit.ui.components.OngoingOrderView;
import com.fixit.ui.fragments.AboutFragment;
import com.fixit.ui.fragments.ProfessionsListFragment;
import com.fixit.ui.fragments.SearchFragment;
import com.fixit.ui.fragments.TradesmanReviewFragment;
import com.fixit.ui.helpers.OrderedTradesmanInteractionHandler;
import com.fixit.ui.helpers.TradesmanActionHandler;
import com.fixit.validation.AddressValidator;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.List;

/**
 * Created by konstantin on 3/29/2017.
 */

public class SearchActivity extends BaseAppActivity<SearchController>
        implements SearchFragment.SearchFragmentInteractionListener,
                   SearchManager.SearchCallback,
                   ProfessionsListFragment.ProfessionSelectionListener,
                   GoogleClientManager.GoogleManagerCallback,
                   NavigationView.OnNavigationItemSelectedListener,
                   OrderedTradesmanInteractionHandler.OrderedTradesmanInteractionListener,
                   TradesmanReviewFragment.TradesmanReviewListener {

    private static final String LOG_TAG = "#" + SearchActivity.class.getSimpleName();
    private static final String FRAG_TAG_SEARCH = "searchFrag";
    private static final String FRAG_TAG_PROFESSIONS = "professionsFrag";

    public static final int DELEGATE_ORDER_HISTORY = 0;

    private GoogleClientManager mGoogleClientManager;
    private AddressValidator mAddressValidator;

    private JobLocation mJobLocation;
    private Profession mProfession;

    private boolean navInitialized = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkActivityDelegate()) {
            setContentView(R.layout.activity_drawer_layout);

            if (savedInstanceState == null) {
                createSearchFragment();
            }

            mGoogleClientManager = new GoogleClientManager(new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API),
                    this,
                    this
            );

            mAddressValidator = new AddressValidator();
        }
    }

    private boolean checkActivityDelegate() {
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(Constants.ARG_DELEGATE)) {
            switch (extras.getInt(Constants.ARG_DELEGATE)) {
                case DELEGATE_ORDER_HISTORY:
                    startActivity(new Intent(this, OrderHistoryActivity.class));
                    return true;
            }
        }
        return false;
    }

    private void createSearchFragment() {
        String profession = null;
        String address = null;

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(Constants.ARG_ORDER_TO_RESTORE)) {
            String orderId = extras.getString(Constants.ARG_ORDER_TO_RESTORE);
            SearchController controller = getController();
            OrderData order = controller.getOrder(orderId);
            profession = controller.getProfession(order.getProfessionId()).getName();
            address = order.getLocation().getGoogleAddress();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left)
                .add(R.id.fragment_holder, SearchFragment.newInstance(profession, address), FRAG_TAG_SEARCH)
                .commit();
    }

    @Override
    public void setToolbar(Toolbar toolbar, boolean homeAsUpEnabled) {
        super.setToolbar(toolbar, homeAsUpEnabled);

        if(!navInitialized) {
            initNavigationDrawer(toolbar);
            navInitialized = true;
        }
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard(getSearchFragment().getView().getRootView().getWindowToken());
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        invalidateNavigationHeader(navigationView);
    }

    private void invalidateNavigationHeader(NavigationView navigationView) {
        SearchController controller = getController();
        OrderData order = controller.getLatestOrder();
        View headerView = navigationView.getHeaderView(0);
        if(order != null && !order.isFeedbackProvided()) {
            final OngoingOrderView ongoingOrderView = (OngoingOrderView) headerView.findViewById(R.id.ongoing_order_view);
            String ongoingOrderId = ongoingOrderView.getOrderId();

            if(!TextUtils.isEmpty(ongoingOrderId) && !ongoingOrderId.equals(order.getId())) {
                controller.loadLatestOrder(new OrderController.SingleOrderCallback() {
                    @Override
                    public void onOrderLoaded(Order order) {
                        ongoingOrderView.setOrder(order);
                        if(ongoingOrderView.getInteractionHandler() == null) {
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
    protected void onStart() {
        super.onStart();

        getSearchFragment().setGoogleApiClient(mGoogleClientManager.mClient);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(navInitialized) {
            invalidateNavigationHeader((NavigationView) findViewById(R.id.nav_view));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(navInitialized) {
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
        return new SearchController((BaseApplication) getApplication(), this);
    }

    private SearchFragment getSearchFragment() {
        return (SearchFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_SEARCH);
    }

    @Override
    public void showProfessionsList() {
        getAnalyticsManager().trackShowProfessions();
        hideKeyboard(getSearchFragment().getView().getRootView().getWindowToken());
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_holder, new ProfessionsListFragment(), FRAG_TAG_PROFESSIONS)
                .commit();
    }

    @Override
    public void onProfessionSelected(Profession profession) {
        getSupportFragmentManager().popBackStack();
        getSearchFragment().onProfessionChosen(profession);
    }

    @Override
    public void showMap() {
        getAnalyticsManager().trackShowMap();
        if(mGoogleClientManager.showPlacePicker(getSearchFragment())) {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }
    }

    @Override
    public void performSearch(String professionName, final String address) {
        mProfession = getController().getProfession(professionName);
        if(mProfession != null) {
            showLoader(getString(R.string.validating_address));
            mAddressValidator.validate(address, new AddressValidator.AddressValidationCallback() {
                @Override
                public void onAddressValidated(AddressValidator.AddressValidationResult result) {
                    if(result.jobLocation != null) {
                        showLoader(getString(R.string.starting_search));
                        mJobLocation = result.jobLocation;
                        double lat = mJobLocation.getLat();
                        double lng = mJobLocation.getLng();
                        getController().sendSearch(
                                SearchActivity.this,
                                mProfession,
                                new MutableLatLng(lat, lng),
                                SearchActivity.this
                        );
                        getAnalyticsManager().trackSearch(mProfession.getName(), address);
                    } else {
                        invalidAddress();
                    }
                }

                @Override
                public void onValidationError(String error, Throwable t) {
                    FILog.e(LOG_TAG, "Address Geocode Validation error: " + error, t, getApplicationContext());
                    invalidAddress();
                }
            });
        } else {
            notifyUser(getString(R.string.invalid_profession));
        }
    }

    @Override
    public void invalidAddress() {
        notifyUser(getString(R.string.invalid_address));
        hideLoader();
    }

    @Override
    public void onSearchStarted(String searchId) {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(Constants.ARG_SEARCH_ID, searchId);
        intent.putExtra(Constants.ARG_JOB_LOCATION, mJobLocation);
        intent.putExtra(Constants.ARG_PROFESSION, mProfession);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        hideLoader();
    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment, String tag) {
        dialogFragment.show(getSupportFragmentManager(), tag);
    }

    @Override
    public Activity getActivityForResult() {
        return this;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeNavigationDrawer();
        switch (item.getItemId()) {
            case R.id.order_history:
                startActivity(new Intent(this, OrderHistoryActivity.class));
                break;
            case R.id.about:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                        .add(R.id.fragment_holder, AboutFragment.newInstance())
                        .commit();
                break;
        }
        return true;
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

}