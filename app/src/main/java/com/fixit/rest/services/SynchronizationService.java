package com.fixit.rest.services;

import com.fixit.rest.apis.SynchronizationServiceAPI;
import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.data.SynchronizationRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.SynchronizationResponseData;

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
