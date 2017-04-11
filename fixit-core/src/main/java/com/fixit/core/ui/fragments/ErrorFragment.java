package com.fixit.core.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.fixit.core.data.DeviceInfo;
import com.fixit.core.data.ServerLog;
import com.fixit.core.data.VersionInfo;
import com.fixit.core.rest.apis.ServerLogDataAPI;
import com.fixit.core.rest.callbacks.EmptyCallback;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.PrefUtils;

import java.util.Date;

/**
 * Created by konstantin on 4/3/2017.
 */

public class ErrorFragment extends BaseFragment<ActivityController> implements View.OnClickListener {

    private ErrorParams mParams;
    private DeviceInfo mDeviceInfo;
    private VersionInfo mVersionInfo;

    private ViewHolder mView;

    private static class ViewHolder {

        final TextView tvDisplayMessage;
        final Button btnReport;
        final Button btnInternetSettings;
        final Button btnContinue;

        ViewHolder(View v, View.OnClickListener onClickListener) {
            tvDisplayMessage = (TextView) v.findViewById(R.id.tv_display_msg);
            btnReport = (Button) v.findViewById(R.id.btn_report_error);
            btnInternetSettings = (Button) v.findViewById(R.id.btn_internet_settings);
            btnContinue = (Button) v.findViewById(R.id.btn_continue);

            btnReport.setOnClickListener(onClickListener);
            btnInternetSettings.setOnClickListener(onClickListener);
            btnContinue.setOnClickListener(onClickListener);
        }

        void setState(ErrorType errorType) {
            switch (errorType) {
                case GENERAL:
                    btnInternetSettings.setVisibility(View.GONE);
                    break;
                case NO_NETWORK:
                    btnReport.setVisibility(View.GONE);
                    break;
            }
        }

    }

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
        mDeviceInfo = AppConfig.getDeviceInfo();
        mVersionInfo = AppConfig.getVersionInfo(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_error, container, false);

        mView = new ViewHolder(v, this);
        mView.setState(mParams.errorType);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sendLog();
    }

    private void sendLog() {
        Context context = getContext();

        ServerLog log = new ServerLog();
        PrefUtils.fillServerLog(context, log);
        log.setDeviceInfo(mDeviceInfo);
        log.setVersionInfo(mVersionInfo);
        log.setLevel(mParams.errorType.name());
        log.setTag(ErrorFragment.class.getSimpleName());
        log.setMessage(mParams.logMsg);
        log.setStackTrace(Log.getStackTraceString(mParams.cause));
        log.setCreatedAt(new Date());

        ServerLogDataAPI logApi = getController().getServerApiFactory().createServerLogApi();
        logApi.create(log).enqueue(new EmptyCallback<ServerLog>());
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if(resId == R.id.btn_continue) {
            continueApp();
        } else if(resId == R.id.btn_report_error) {
            reportError();
        } else if(resId == R.id.btn_internet_settings) {
            showInternetSettings();
        }
    }

    private void continueApp() {

    }

    private void reportError() {

    }

    private void showInternetSettings() {

    }

    // ERROR TYPES, PARAMS, BUILDERS

    public enum ErrorType {
        GENERAL,
        NO_NETWORK
    }

    public static ErrorParamsBuilder getGeneralBuilder(String userMsg) {
        return new ErrorParamsBuilder(ErrorType.GENERAL, userMsg);
    }

    public static ErrorParamsBuilder getGeneralBuilder(Context context) {
        return new ErrorParamsBuilder(ErrorType.GENERAL, context);
    }

    public static ErrorParamsBuilder getBuilder(ErrorType type, String userMsg) {
        return new ErrorParamsBuilder(type, userMsg);
    }

    public static ErrorParamsBuilder getBuilder(ErrorType type, Context context) {
        return new ErrorParamsBuilder(type, context);
    }

    public static class ErrorParams implements Parcelable {
        public final ErrorType errorType;
        public final String userMsg;
        public final String logMsg;
        public final Throwable cause;

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

        protected ErrorParams(Parcel in) {
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
            this.userMsg = AppConfig.getString(context, AppConfig.KEY_ERROR_DEFAULT_DISPLAY_MSG, "");
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

        public ErrorParams build() {
            if(cause == null) {
                cause = new Throwable(logMsg);
            }
            return new ErrorParams(this);
        }

    }
}
