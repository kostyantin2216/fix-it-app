package com.fixit.core.rest.services;

import com.fixit.core.rest.apis.AppServiceAPI;
import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.data.SearchRequestData;
import com.fixit.core.rest.requests.data.SearchResultRequestData;
import com.fixit.core.rest.requests.data.SynchronizationRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.SearchResponseData;
import com.fixit.core.rest.responses.data.SearchResultResponseData;
import com.fixit.core.rest.responses.data.SynchronizationResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public interface AppServiceService {

    @POST(AppServiceAPI.API_NAME + AppServiceAPI.EP_SYNCHRONIZE)
    Call<APIResponse<SynchronizationResponseData>> synchronize(@Body APIRequest<SynchronizationRequestData> request);

    @POST(AppServiceAPI.API_NAME + AppServiceAPI.EP_SEARCH)
    Call<APIResponse<SearchResponseData>> beginSearch(@Body APIRequest<SearchRequestData> request);

    @POST(AppServiceAPI.API_NAME + AppServiceAPI.EP_RESULTS)
    Call<APIResponse<SearchResultResponseData>> fetchResults(@Body APIRequest<SearchResultRequestData> request);

}
