package com.fixit.core.rest.callbacks;

import android.content.Context;

import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.APIResponseHeader;
import com.fixit.core.utils.Constants;

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
        APIResponse<RD> apiResponse = response.body();
        if(response.code() == Constants.HTTP_INTERNAL_SERVER_ERROR) {
            onServerError();
        } else {
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
