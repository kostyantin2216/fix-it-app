package com.fixit.rest.callbacks;

import com.fixit.rest.APIError;

import java.util.List;

/**
 * Created by konstantin on 3/30/2017.
 */

public interface ServiceErrorCallback {
    void onAppServiceError(List<APIError> errors);
    void onServerError();
}
