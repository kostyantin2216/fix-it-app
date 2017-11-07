package com.fixit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.fixit.FixItApplication;
import com.fixit.app.R;
import com.fixit.controllers.SearchController;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.external.google.GoogleClientManager;
import com.fixit.ui.fragments.ProfessionsListFragment;
import com.fixit.ui.fragments.SearchFragment;
import com.fixit.utils.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

/**
 * Created by konstantin on 3/29/2017.
 */
public class CombinedSearchActivity extends SearchActivity
        implements SearchFragment.SearchFragmentInteractionListener,
                   ProfessionsListFragment.ProfessionSelectionListener {

    private static final String FRAG_TAG_SEARCH = "searchFrag";
    private static final String FRAG_TAG_PROFESSIONS = "professionsFrag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            createSearchFragment();
        }
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
    protected void onStart() {
        super.onStart();

        getSearchFragment().setGoogleApiClient(getGoogleClientManager().mClient);
    }

    @Override
    public SearchController createController() {
        return new SearchController((FixItApplication) getApplication(), this);
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
        if(getGoogleClientManager().showPlacePicker(getSearchFragment())) {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }
    }

    @Override
    public void performSearch(String profession, String address) {
        getController().performSearch(this, profession, address, this);
        showLoader(getString(R.string.validating_address));
    }

}
