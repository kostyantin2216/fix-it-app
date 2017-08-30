package com.fixit.rest.callbacks;

import android.content.Context;

import com.fixit.rest.responses.TwilioErrorResponse;
import com.fixit.utils.FILog;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by konstantin on 5/16/2017.
 */

public abstract class TwilioCallback<TR> extends RetryingCallback<TR> {
    public TwilioCallback(Context context) {
        super(context);
    }

    public TwilioCallback(int retryLimit, int retryIntervalMs) {
        super(retryLimit, retryIntervalMs);
    }

    @Override
    public void onResponse(Call<TR> call, Response<TR> response) {
        if(response.isSuccessful()) {
            onResponse(call, response.body());
        } else {
            try {
                String jsonError = response.errorBody().string();
                onError(call, new Gson().fromJson(jsonError, TwilioErrorResponse.class));
            } catch (IOException e) {
                FILog.e("could not get twilio error body from response.", e);
            }
        }
    }

    public abstract void onResponse(Call<TR> call, TR response);
    public abstract void onError(Call<TR> call, TwilioErrorResponse errorResponse);

}
