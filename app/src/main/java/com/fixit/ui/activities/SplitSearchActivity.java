package com.fixit.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fixit.app.R;
import com.fixit.data.Profession;
import com.fixit.ui.fragments.LocationPickerFragment;
import com.fixit.ui.fragments.ProfessionPickerFragment;

public class SplitSearchActivity extends SearchActivity implements ProfessionPickerFragment.ProfessionSelectionListener {

    private final static String FRAG_TAG_PROFESSION_PICKER = "profession_picker";
    private final static String FRAG_TAG_LOCATION_PICKER = "location_picker";

    private Profession mProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.exit_out_right, R.anim.enter_from_left)
                    .add(R.id.fragment_holder, ProfessionPickerFragment.newInstance(null), FRAG_TAG_PROFESSION_PICKER)
                    .commit();
        }
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

    @Nullable
    private ProfessionPickerFragment getProfessionPickerFragment() {
        return (ProfessionPickerFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_PROFESSION_PICKER);
    }
}
