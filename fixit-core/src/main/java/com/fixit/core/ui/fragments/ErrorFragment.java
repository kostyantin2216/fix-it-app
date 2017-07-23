package com.fixit.core.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fixit.core.R;
import com.fixit.core.config.AppConfig;
import com.fixit.core.controllers.ActivityController;
import com.fixit.core.rest.APIError;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.ErrorUtils;
import com.fixit.core.utils.FILog;
import com.fixit.core.utils.GlobalPreferences;

import java.util.Iterator;
import java.util.List;

/**
 * Created by konstantin on 4/3/2017.
 */

public class ErrorFragment extends BaseFragment<ActivityController> implements View.OnClickListener {

    private final static String LOG_TAG = "#" + ErrorFragment.class.getSimpleName();

    private ErrorParams mParams;

    private ViewHolder mView;

    public static ErrorFragment newInstance(ErrorParams params) {
        ErrorFragment errorFragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_ERROR_PARAMS, params);
        errorFragment.setArguments(args);
        return errorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParams = getArguments().getParcelable(Constants.ARG_ERROR_PARAMS);

        if(mParams == null) {
            throw ErrorUtils.missingArguments(
                    new ErrorUtils.MissingValuesArgument(getClass(), 1)
                        .add(Constants.ARG_ERROR_PARAMS, ErrorParams.class)
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_error, container, false);

        mView = new ViewHolder(v, this);
        mView.setState(mParams.errorType);
        mView.tvDisplayMessage.setText(mParams.userMsg);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FILog.e(LOG_TAG, mParams.logMsg, mParams.cause, getContext());
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if(resId == R.id.btn_continue) {
            continueApp();
        } else if(resId == R.id.btn_close) {
            hideSelf();
        } else if(resId == R.id.btn_report_error) {
            reportError();
        } else if(resId == R.id.btn_wifi_settings) {
            showWifiSettings();
        } else if(resId == R.id.btn_mobile_networks) {
            showMobileNetworkSettings();
        } else if(resId == R.id.btn_exit_app) {
            exitApp();
        } else if(resId == R.id.btn_developer_settings) {
            openDeveloperSettings();
        }
    }

    // ACTIONS

    private void exitApp() {
        getActivity().finishAffinity();;
    }

    private void continueApp() {
        Context context = getContext().getApplicationContext();
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        getActivity().finishAffinity();
    }

    private void hideSelf() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void reportError() {
        Context context = getContext();
        Intent intent = new Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", AppConfig.getString(context, AppConfig.KEY_EMAIL_FOR_SUPPORT, "kostya2216@gmail.com"), null)
        );
        intent.putExtra(Intent.EXTRA_SUBJECT, AppConfig.getString(context, AppConfig.KEY_SUBJECT_FOR_ERROR_REPORT, "Error Report"));
        intent.putExtra(Intent.EXTRA_TEXT, generateReportMessage());
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private String generateReportMessage() {
        Context context = getContext();
        return AppConfig.getVersionInfo(context).toString() + "\n\n"
                + AppConfig.getDeviceInfo(context).toString() + "\n\n"
                + "UserId = " + GlobalPreferences.getUserId(context) + ", InstallationId = " + GlobalPreferences.getInstallationId(context) + "\n\n"
                + "logMessage = " + mParams.logMsg + "\n"
                + "StackTrace = \n" + Log.getStackTraceString(mParams.cause);
    }

    private void showWifiSettings() {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    private void showMobileNetworkSettings() {
        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
    }

    private void openDeveloperSettings() {
        // TODO: need to create developer settings intent.
        //startActivity(new Intent(this, DeveloperSe));
    }

    // VIEW

    private static class ViewHolder {

        final TextView tvDisplayMessage;
        final Button btnReport;
        final Button btnWifiSettings;
        final Button btnMobileNetworks;
        final Button btnClose;
        final Button btnContinue;
        final Button btnExitApp;
        final Button btnDeveloperSettings;

        ViewHolder(View v, View.OnClickListener onClickListener) {
            tvDisplayMessage = (TextView) v.findViewById(R.id.tv_display_msg);
            btnReport = (Button) v.findViewById(R.id.btn_report_error);
            btnWifiSettings = (Button) v.findViewById(R.id.btn_wifi_settings);
            btnMobileNetworks = (Button) v.findViewById(R.id.btn_mobile_networks);
            btnClose = (Button) v.findViewById(R.id.btn_close);
            btnContinue = (Button) v.findViewById(R.id.btn_continue);
            btnExitApp = (Button) v.findViewById(R.id.btn_exit_app);
            btnDeveloperSettings = (Button) v.findViewById(R.id.btn_developer_settings);

            btnReport.setOnClickListener(onClickListener);
            btnWifiSettings.setOnClickListener(onClickListener);
            btnMobileNetworks.setOnClickListener(onClickListener);
            btnClose.setOnClickListener(onClickListener);
            btnContinue.setOnClickListener(onClickListener);
            btnExitApp.setOnClickListener(onClickListener);
        }

        void setState(ErrorType errorType) {
            switch (errorType) {
                case NO_NETWORK:
                    btnWifiSettings.setVisibility(View.VISIBLE);
                    btnMobileNetworks.setVisibility(View.VISIBLE);
                    btnContinue.setVisibility(View.VISIBLE);
                    break;
                case GENERAL:
                    btnReport.setVisibility(View.VISIBLE);
                    btnContinue.setVisibility(View.VISIBLE);
                    break;
                case SERVER_UNAVAILABLE:
                    btnExitApp.setVisibility(View.VISIBLE);
                    break;
                case PROMPT:
                    btnClose.setVisibility(View.VISIBLE);
                    break;
            }
            if(!AppConfig.isProduction(btnDeveloperSettings.getContext())) {
                btnDeveloperSettings.setVisibility(View.VISIBLE);
            }
        }

    }

    // ERROR TYPES, PARAMS, BUILDERS

    public enum ErrorType {
        PROMPT(R.string.error_default_display_msg),
        GENERAL(R.string.error_default_display_msg),
        NO_NETWORK(R.string.error_no_network_msg),
        SERVER_UNAVAILABLE(R.string.error_server_unavailable);

        final int defaultDisplayMsgResId;

        ErrorType(int defaultDisplayMsgResId) {
            this.defaultDisplayMsgResId = defaultDisplayMsgResId;
        }

        public ErrorParamsBuilder createBuilder(String userMsg) {
            return new ErrorParamsBuilder(this, userMsg);
        }

        public ErrorParamsBuilder createBuilder(Context context) {
            return new ErrorParamsBuilder(this, context);
        }
    }

    public static class ErrorParams implements Parcelable {
        final ErrorType errorType;
        final String userMsg;
        final String logMsg;
        final Throwable cause;

        private ErrorParams(ErrorParamsBuilder builder) {
            this.errorType = builder.errorType;
            this.userMsg = builder.userMsg;
            this.logMsg = builder.logMsg;
            this.cause = builder.cause;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.errorType == null ? -1 : this.errorType.ordinal());
            dest.writeString(this.userMsg);
            dest.writeString(this.logMsg);
            dest.writeSerializable(this.cause);
        }

        ErrorParams(Parcel in) {
            int tmpErrorType = in.readInt();
            this.errorType = tmpErrorType == -1 ? null : ErrorType.values()[tmpErrorType];
            this.userMsg = in.readString();
            this.logMsg = in.readString();
            this.cause = (Throwable) in.readSerializable();
        }

        public static final Creator<ErrorParams> CREATOR = new Creator<ErrorParams>() {
            @Override
            public ErrorParams createFromParcel(Parcel source) {
                return new ErrorParams(source);
            }

            @Override
            public ErrorParams[] newArray(int size) {
                return new ErrorParams[size];
            }
        };
    }

    public static class ErrorParamsBuilder {
        private ErrorType errorType;
        private String userMsg;
        private String logMsg = "";
        private Throwable cause;

        private ErrorParamsBuilder(ErrorType errorType, String userMsg) {
            this.errorType = errorType;
            this.userMsg = userMsg;
        }

        private ErrorParamsBuilder(ErrorType errorType, Context context) {
            this.errorType = errorType;
            this.userMsg = context.getString(errorType.defaultDisplayMsgResId);
        }

        public ErrorParamsBuilder display(String text) {
            this.userMsg = text;
            return this;
        }

        public ErrorParamsBuilder log(String text) {
            this.logMsg = text;
            return this;
        }

        public ErrorParamsBuilder cause(Throwable t) {
            this.cause = t;
            return this;
        }

        public ErrorParamsBuilder apiError(List<APIError> errors) {
            StringBuilder sb = new StringBuilder();
            Iterator<APIError> itr = errors.iterator();
            while(itr.hasNext()) {
                sb.append(itr.next().toString());
                if(itr.hasNext()) {
                    sb.append("\n");
                }
            }
            this.logMsg = sb.toString();
            return this;
        }

        public ErrorParams build() {
            if(cause == null) {
                cause = new Throwable(logMsg);
            }
            return new ErrorParams(this);
        }

    }

}
