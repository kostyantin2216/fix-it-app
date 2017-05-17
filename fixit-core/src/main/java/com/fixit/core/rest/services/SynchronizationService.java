package com.fixit.core.rest.services;

import com.fixit.core.rest.apis.SynchronizationServiceAPI;
import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.data.SynchronizationRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.SynchronizationResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Kostyantin on 4/12/2017.
 */

public interface SynchronizationService {

    @POST(SynchronizationServiceAPI.API_NAME)
    Call<APIResponse<SynchronizationResponseData>> synchronize(@Body APIRequest<SynchronizationRequestData> request);

}
