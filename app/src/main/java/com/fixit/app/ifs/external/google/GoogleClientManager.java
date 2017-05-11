package com.fixit.app.ifs.external.google;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.fixit.core.ui.listeners.DialogListener;
import com.fixit.core.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import static android.app.Activity.RESULT_OK;

/**
 * Created by konstantin on 5/10/2017.
 */

public class GoogleClientManager implements GoogleApiClient.OnConnectionFailedListener, DialogListener {

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";

    public final GoogleApiClient mClient;

    private final GoogleManagerCallback mCallback;

    private boolean mResolvingError = false;

    public GoogleClientManager(GoogleApiClient.Builder builder, FragmentActivity autoManager, GoogleManagerCallback callback) {
        mClient = builder
                .enableAutoManage(autoManager, this)
                .build();
        mCallback = callback;
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

    public void onActivityResult(int requestCode, int resultCode) {
        if(requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mClient.isConnecting() && !mClient.isConnected()) {
                    mClient.connect();
                }
            }
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

    public interface GoogleManagerCallback {
        void showDialogFragment(DialogFragment dialogFragment, String tag);
        Activity getActivityForResult();
    }
}
