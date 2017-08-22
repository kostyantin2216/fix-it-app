package com.fixit.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fixit.app.R;
import com.fixit.app.ifs.external.google.GoogleClientManager;
import com.fixit.app.ifs.validation.AddressValidator;
import com.fixit.app.ui.components.OngoingOrderView;
import com.fixit.app.ui.fragments.AboutFragment;
import com.fixit.app.ui.helpers.OrderedTradesmanInteractionHandler;
import com.fixit.app.ui.fragments.ProfessionsListFragment;
import com.fixit.app.ui.fragments.SearchFragment;
import com.fixit.app.ui.fragments.TradesmanProfileFragment;
import com.fixit.app.ui.fragments.TradesmanReviewFragment;
import com.fixit.app.ui.helpers.TradesmanActionHandler;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.SearchController;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.MutableLatLng;
import com.fixit.core.data.Order;
import com.fixit.core.data.Profession;
import com.fixit.core.data.Review;
import com.fixit.core.data.Tradesman;
import com.fixit.core.general.SearchManager;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.FILog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

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
        setContentView(R.layout.activity_drawer_layout);


        if(savedInstanceState == null) {
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

        checkDelegate();
    }

    private void checkDelegate() {
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(Constants.ARG_DELEGATE)) {
            switch (extras.getInt(Constants.ARG_DELEGATE)) {
                case DELEGATE_ORDER_HISTORY:
                    startActivity(new Intent(this, OrderHistoryActivity.class));
                    break;
            }
        }
    }

    private void createSearchFragment() {
        String profession = null;
        String address = null;

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(Constants.ARG_ORDER_TO_RESTORE)) {
            long orderId = extras.getLong(Constants.ARG_ORDER_TO_RESTORE);
            Order order = getController().getOrder(orderId);
            profession = order.getProfession().getName();
            address = order.getJobLocation().getGoogleAddress();
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
        Order order = getController().getLatestOrder();

        View headerView = navigationView.getHeaderView(0);
        if(order != null && !order.isComplete()) {
            OngoingOrderView ongoingOrderView = (OngoingOrderView) headerView.findViewById(R.id.ongoing_order_view);
            ongoingOrderView.setOrder(order);
            ongoingOrderView.setInteractionHandler(new OrderedTradesmanInteractionHandler(this));
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
    public void onProfessionSelect(Profession profession) {
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
