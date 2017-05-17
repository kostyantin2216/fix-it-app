package com.fixit.core.rest.services;

import com.fixit.core.rest.apis.SearchServiceAPI;
import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.data.SearchRequestData;
import com.fixit.core.rest.requests.data.SearchResultRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.SearchResponseData;
import com.fixit.core.rest.responses.data.SearchResultResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Kostyantin on 4/12/2017.
 */

public interface SearchService {

    @POST(SearchServiceAPI.API_NAME + "/startTradesmanSearch")
    Call<APIResponse<SearchResponseData>> beginSearch(@Body APIRequest<SearchRequestData> request);

    @POST(SearchServiceAPI.API_NAME + "/fetchTradesmenResults")
    Call<APIResponse<SearchResultResponseData>> fetchResults(@Body APIRequest<SearchResultRequestData> request);

}
