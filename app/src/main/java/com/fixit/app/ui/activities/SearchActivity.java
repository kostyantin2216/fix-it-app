package com.fixit.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.fixit.app.R;
import com.fixit.app.ifs.external.google.GoogleClientManager;
import com.fixit.app.ifs.validation.AddressValidator;
import com.fixit.app.ui.fragments.ProfessionsListFragment;
import com.fixit.app.ui.fragments.SearchFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.SearchController;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.MutableLatLng;
import com.fixit.core.data.Profession;
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
                   GoogleClientManager.GoogleManagerCallback {

    private static final String LOG_TAG = "#" + SearchActivity.class.getSimpleName();
    private static final String FRAG_TAG_SEARCH = "searchFrag";
    private static final String FRAG_TAG_PROFESSIONS = "professionsFrag";

    private GoogleClientManager mGoogleClientManager;
    private AddressValidator mAddressValidator;

    private JobLocation mJobLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_holder);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left)
                    .add(R.id.fragment_holder, new SearchFragment(), FRAG_TAG_SEARCH)
                    .commit();
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

    @Override
    protected void onStart() {
        super.onStart();

        getSearchFragment().setGoogleApiClient(mGoogleClientManager.mClient);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleClientManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public SearchController createController() {
        return new SearchController((BaseApplication) getApplication());
    }

    private SearchFragment getSearchFragment() {
        return (SearchFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_SEARCH);
    }

    @Override
    public void showProfessionsList() {
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
        if(mGoogleClientManager.showPlacePicker(getSearchFragment())) {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }
    }

    @Override
    public void performSearch(String professionName, String address) {
        final Profession profession = getController().getProfession(professionName);
        if(profession != null) {
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
                                profession,
                                new MutableLatLng(lat, lng),
                                SearchActivity.this
                        );
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

}
