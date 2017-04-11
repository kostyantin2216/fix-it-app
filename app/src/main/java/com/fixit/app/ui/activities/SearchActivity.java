package com.fixit.app.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.ProfessionsListFragment;
import com.fixit.app.ui.fragments.SearchFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.SearchController;
import com.fixit.core.data.Profession;
import com.fixit.core.general.SearchManager;
import com.fixit.core.ui.activities.BaseActivity;
import com.fixit.core.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by konstantin on 3/29/2017.
 */

public class SearchActivity extends BaseActivity<SearchController>
        implements GoogleApiClient.OnConnectionFailedListener,
                   SearchFragment.SearchFragmentInteractionListener,
                   SearchManager.SearchCallback,
                   ProfessionsListFragment.ProfessionSelectionListener {

    private static final String FRAG_TAG_SEARCH = "searchFrag";
    private static final String FRAG_TAG_PROFESSIONS = "professionsFrag";

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final int REQUEST_PLACE_PICKER = 1002;
    private static final String DIALOG_ERROR = "dialog_error";

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

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

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getSearchFragment().setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_RESOLVE_ERROR:
                mResolvingError = false;
                if (resultCode == RESULT_OK) {
                    // Make sure the app is not already connected or attempting to connect
                    if (!mGoogleApiClient.isConnecting() &&
                            !mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.connect();
                    }
                }
                break;
            case REQUEST_PLACE_PICKER:
                if(resultCode == RESULT_OK) {
                    getSearchFragment().onPlaceChosen(PlacePicker.getPlace(this, data));
                }
                break;
        }
    }

    @Override
    public SearchController createController() {
        return new SearchController((BaseApplication) getApplication());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!mResolvingError) {
            if (result.hasResolution()) {
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    // There was an error with the resolution intent. Try again.
                    mGoogleApiClient.connect();
                }
            } else {
                // Show dialog using GoogleApiAvailability.getErrorDialog()
                showErrorDialog(result.getErrorCode());
                mResolvingError = true;
            }
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        GoogleErrorDialogFragment dialogFragment = new GoogleErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
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
        try {
            startActivityForResult(new PlacePicker.IntentBuilder().build(this), REQUEST_PLACE_PICKER);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performSearch(String professionName, String address) {
        Profession profession = getController().getProfession(professionName);
        if(profession != null) {
            getController().sendSearch(this, profession, address, this);
        } else {
            notifyUser(getString(R.string.invalid_profession));
        }
    }

    @Override
    public void invalidAddress() {
        notifyUser(getString(R.string.invalid_address));
    }

    @Override
    public void onSearchStarted(String searchId) {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra(Constants.ARG_SEARCH_ID, searchId);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
    }

    public static class GoogleErrorDialogFragment extends DialogFragment {
        public GoogleErrorDialogFragment() { }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((SearchActivity) getActivity()).onDialogDismissed();
        }
    }

}
