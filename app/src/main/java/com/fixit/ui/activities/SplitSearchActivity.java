package com.fixit.ui.activities;

import android.location.Address;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.fixit.app.R;
import com.fixit.data.JobLocation;
import com.fixit.data.Profession;
import com.fixit.general.IntentHandler;
import com.fixit.ui.fragments.LocationPickerFragment;
import com.fixit.ui.fragments.ProfessionPickerFragment;
import com.fixit.utils.Constants;

public class SplitSearchActivity extends SearchActivity
        implements ProfessionPickerFragment.ProfessionSelectionListener,
                   LocationPickerFragment.LocationPickerFragmentInteractionListener {

    private final static String FRAG_TAG_PROFESSION_PICKER = "profession_picker";
    private final static String FRAG_TAG_LOCATION_PICKER = "location_picker";

    private Profession mProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            if(!IntentHandler.handle(this)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.exit_out_right, R.anim.enter_from_left)
                        .add(R.id.fragment_holder, ProfessionPickerFragment.newInstance(null), FRAG_TAG_PROFESSION_PICKER)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.ARG_PROFESSION, mProfession);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mProfession = savedInstanceState.getParcelable(Constants.ARG_PROFESSION);
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateLocationPickerFragment();
    }

    @Override
    public void onBackPressed() {
        if(mProfession != null) {
            hideKeyboard(getRootView().getWindowToken());
            String defaultProfession = mProfession.getName();
            mProfession = null;

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.exit_out_right, R.anim.enter_from_left)
                    .replace(R.id.fragment_holder, ProfessionPickerFragment.newInstance(defaultProfession), FRAG_TAG_PROFESSION_PICKER)
                    .commit();

            getRootView().post(() -> initNavigationDrawer((Toolbar) findViewById(R.id.toolbar)));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onProfessionSelected(Profession profession) {
        mProfession = profession;
        hideKeyboard(getRootView().getWindowToken());

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.exit_out_right, R.anim.enter_from_left)
                .replace(R.id.fragment_holder, LocationPickerFragment.newInstance(mProfession), FRAG_TAG_LOCATION_PICKER)
                .commit();

        getRootView().post(this::updateLocationPickerFragment);
    }

    private void updateLocationPickerFragment() {
        LocationPickerFragment locationPickerFragment = getLocationPickerFragment();
        if(locationPickerFragment != null) {
            locationPickerFragment.setGoogleApiClient(getGoogleClientManager().mClient);
        }
    }

    @Nullable
    private LocationPickerFragment getLocationPickerFragment() {
        return (LocationPickerFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_LOCATION_PICKER);
    }

    @Override
    public void performSearch(String profession, Address address) {
        getController().sendSearch(this, mProfession, JobLocation.create(address), this);
        showLoader(getString(R.string.starting_search));
    }
}
