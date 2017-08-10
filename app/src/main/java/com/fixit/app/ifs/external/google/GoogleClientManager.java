package com.fixit.app.ifs.external.google;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.fixit.core.ui.listeners.DialogListener;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.FILog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by konstantin on 5/10/2017.
 */

public class GoogleClientManager implements GoogleApiClient.OnConnectionFailedListener, DialogListener {

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final int REQUEST_LOGIN = 1002;
    private static final int REQUEST_PLACE_PICKER = 1003;
    private static final String DIALOG_ERROR = "dialog_error";

    public final GoogleApiClient mClient;

    private final GoogleManagerCallback mCallback;
    private GoogleSignInCallback mSignInCallback;
    private GooglePlacesCallback mPlacesCallback;

    private boolean mResolvingError = false;

    public GoogleClientManager(GoogleApiClient.Builder builder, FragmentActivity autoManager, GoogleManagerCallback callback) {
        mClient = builder
                .enableAutoManage(autoManager, this)
                .build();
        mCallback = callback;
    }

    public void login(Fragment fragmentForResult, GoogleSignInCallback signInCallback) {
        mSignInCallback = signInCallback;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mClient);
        fragmentForResult.startActivityForResult(signInIntent, REQUEST_LOGIN);
    }

    public boolean showPlacePicker(GooglePlacesCallback placesCallback) {
        try {
            Activity fromActivity = mCallback.getActivityForResult();
            fromActivity.startActivityForResult(new PlacePicker.IntentBuilder().build(fromActivity), REQUEST_PLACE_PICKER);
            mPlacesCallback = placesCallback;
            return true;
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!mResolvingError) {
            if (result.hasResolution()) {
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(
                            mCallback.getActivityForResult(),
                            REQUEST_RESOLVE_ERROR
                    );
                } catch (IntentSender.SendIntentException e) {
                    // There was an error with the resolution intent. Try again.
                    mClient.connect();
                }
            } else {
                // Show dialog using GoogleApiAvailability.getErrorDialog()
                showErrorDialog(result.getErrorCode());
                mResolvingError = true;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_RESOLVE_ERROR:
                mResolvingError = false;
                if (resultCode == RESULT_OK) {
                    // Make sure the app is not already connected or attempting to connect
                    if (!mClient.isConnecting() && !mClient.isConnected()) {
                        mClient.connect();
                    }
                }
                break;
            case REQUEST_LOGIN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                FILog.i("google sign in result status = " + result.getStatus());
                if(result.isSuccess()) {
                    mSignInCallback.onSignInSuccess(result.getSignInAccount());
                } else {
                    FILog.e("Google login", result.getStatus().getStatusCode() + " : " + result.getStatus().getStatusMessage());
                    mSignInCallback.onSignInError(result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED);
                }
                mSignInCallback = null;
                break;
            case REQUEST_PLACE_PICKER:
                if(resultCode == RESULT_OK) {
                    mPlacesCallback.onPlaceChosen(PlacePicker.getPlace(mCallback.getActivityForResult(), data));
                }
                mPlacesCallback = null;
                break;
        }
    }

    private void showErrorDialog(int errorCode) {
        GoogleErrorDialogFragment dialogFragment = GoogleErrorDialogFragment.newInstance(this);
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        mCallback.showDialogFragment(dialogFragment, "errordialog");
    }

    @Override
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class GoogleErrorDialogFragment extends DialogFragment {
        private DialogListener mListener;

        public static GoogleErrorDialogFragment newInstance(DialogListener listener) {
            GoogleErrorDialogFragment gedf = new GoogleErrorDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable(Constants.ARG_LISTENER, listener);
            gedf.setArguments(args);
            return gedf;
        }

        public GoogleErrorDialogFragment() { }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            mListener = (DialogListener) getArguments().getSerializable(Constants.ARG_LISTENER);
        }

        @Override
        public void onDetach() {
            super.onDetach();

            mListener = null;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if(mListener != null) {
                mListener.onDialogDismissed();
            }
        }
    }

    public interface GoogleSignInCallback {
        void onSignInSuccess(GoogleSignInAccount account);
        void onSignInError(boolean wasCancelled);
    }

    public interface GooglePlacesCallback {
        void onPlaceChosen(Place place);
    }

    public interface GoogleManagerCallback {
        void showDialogFragment(DialogFragment dialogFragment, String tag);
        Activity getActivityForResult();
    }
}
