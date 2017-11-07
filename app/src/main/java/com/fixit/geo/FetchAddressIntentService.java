package com.fixit.geo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.fixit.general.BiFunction;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.google.android.gms.maps.model.LatLng;

public class FetchAddressIntentService extends IntentService {

    private final BiFunction<Context, String, Result> mTextToAddress;
    private final BiFunction<Context, LatLng, Result> mLatLngToAddress;

    public FetchAddressIntentService() {
        super(Constants.LOG_TAG_FETCH_ADDRESS);
        mTextToAddress = new TextToAddress();
        mLatLngToAddress = new LatLngToAddress();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra(Constants.ARG_RESULT_RECEIVER);
        if (receiver == null) {
            FILog.wtf(Constants.LOG_TAG_FETCH_ADDRESS, "No receiver received. There is nowhere to send the results.");
            return;
        }

        Params params = intent.getParcelableExtra(Constants.ARG_PARAMS);
        if(params == null) {
            Log.wtf(Constants.LOG_TAG_FETCH_ADDRESS, "No params received. No way to find location");
            return;
        }

        Result result;
        if(params.latLng != null) {
            result = mLatLngToAddress.apply(this, params.latLng);
        } else {
            result = mTextToAddress.apply(this, params.address);
        }

        Bundle data = new Bundle();
        if(result.hasError()) {
            data.putString(Constants.ARG_RESULT_DATA, result.error);
            receiver.send(Constants.RESULT_FAILURE, data);
        } else {
            data.putParcelable(Constants.ARG_RESULT_DATA, result.address);
            receiver.send(Constants.RESULT_SUCCESS, data);
        }
    }

    public static class Params implements Parcelable {
        private final LatLng latLng;
        private final String address;

        public Params(LatLng latLng) {
            this.latLng = latLng;
            this.address = null;
        }

        public Params(String address) {
            this.latLng = null;
            this.address = address;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.latLng, flags);
            dest.writeString(this.address);
        }

        protected Params(Parcel in) {
            this.latLng = in.readParcelable(LatLng.class.getClassLoader());
            this.address = in.readString();
        }

        public static final Creator<Params> CREATOR = new Creator<Params>() {
            @Override
            public Params createFromParcel(Parcel source) {
                return new Params(source);
            }

            @Override
            public Params[] newArray(int size) {
                return new Params[size];
            }
        };
    }

    public static class Result implements Parcelable {
        private final Address address;
        private final String error;

        public Result(Address address) {
            this.address = address;
            this.error = null;
        }

        public Result(String error) {
            this.address = null;
            this.error = error;
        }

        public Address getAddress() {
            return address;
        }

        public String getError() {
            return error;
        }

        public boolean hasError() {
            return address == null || !TextUtils.isEmpty(error);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.address, flags);
            dest.writeString(this.error);
        }

        protected Result(Parcel in) {
            this.address = in.readParcelable(Address.class.getClassLoader());
            this.error = in.readString();
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel source) {
                return new Result(source);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };
    }
}
