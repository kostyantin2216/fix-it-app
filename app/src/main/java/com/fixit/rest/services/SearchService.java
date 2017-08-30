package com.fixit.rest.services;

import com.fixit.rest.apis.SearchServiceAPI;
import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.data.SearchRequestData;
import com.fixit.rest.requests.data.SearchResultRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.SearchResponseData;
import com.fixit.rest.responses.data.SearchResultResponseData;

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
