package com.fixit.core.rest.callbacks;

import android.content.Context;

import com.fixit.core.rest.APIError;
import com.fixit.core.rest.responses.APIResponse;

import java.util.List;

import retrofit2.Call;

/**
 * Created by konstantin on 5/15/2017.
 */

public abstract class ManagedServiceCallback<RD> extends ServiceCallback<RD> {

    private final String unexpectedErrorMessage;
    private final GeneralServiceErrorCallback errorCallback;

    public ManagedServiceCallback(Context context, GeneralServiceErrorCallback errorCallback) {
        this(context, errorCallback, "an unexpected error has occurred");
    }

    public ManagedServiceCallback(Context context, GeneralServiceErrorCallback errorCallback, String unexpectedErrorMessage) {
        super(context);
        this.errorCallback = errorCallback;
        this.unexpectedErrorMessage = unexpectedErrorMessage;
    }

    @Override
    public void onAppServiceError(List<APIError> errors) {
        this.errorCallback.onAppServiceError(errors);
    }

    @Override
    public void onRetryFailure(Call<APIResponse<RD>> call, Throwable t) {
        this.errorCallback.onUnexpectedErrorOccurred(unexpectedErrorMessage, t);
    }
}
