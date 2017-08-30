package com.fixit.rest.callbacks;

import android.content.Context;

import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.APIResponseHeader;
import com.fixit.utils.Constants;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by konstantin on 3/30/2017.
 */

public abstract class ServiceCallback<RD> extends RetryingCallback<APIResponse<RD>> implements ServiceErrorCallback {

    public ServiceCallback(Context context) {
        super(context);
    }

    public ServiceCallback(int retryLimit, int retryIntervalMs) {
        super(retryLimit, retryIntervalMs);
    }

    @Override
    public void onResponse(Call<APIResponse<RD>> call, Response<APIResponse<RD>> response) {
        if(response != null && response.code() == Constants.HTTP_INTERNAL_SERVER_ERROR) {
            onRetryFailure(call, new RuntimeException("HTTP Internal Server Error"));
        } else if(response == null || !response.isSuccessful()) {
            onServerError();
        } else {
            APIResponse<RD> apiResponse = response.body();
            APIResponseHeader header = apiResponse.getHeader();
            if(header.hasErrors()) {
                onAppServiceError(header.getErrors());
            } else {
                onResponse(apiResponse.getData());
            }
        }
    }

    public abstract void onResponse(RD responseData);

}
