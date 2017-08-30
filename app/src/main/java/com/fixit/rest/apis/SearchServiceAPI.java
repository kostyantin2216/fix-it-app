package com.fixit.rest.apis;

import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.APIRequestHeader;
import com.fixit.rest.requests.data.SearchRequestData;
import com.fixit.rest.requests.data.SearchResultRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.SearchResponseData;
import com.fixit.rest.responses.data.SearchResultResponseData;
import com.fixit.rest.services.SearchService;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Kostyantin on 4/12/2017.
 */

public class SearchServiceAPI extends BaseServiceAPI {

    public final static String API_NAME = "services/search";

    private final SearchService mService;

    public SearchServiceAPI(APIRequestHeader header, SearchService service) {
        super(header);
        mService = service;
    }

    public void beginSearch(SearchRequestData data, Callback<APIResponse<SearchResponseData>> callback) {
        mService.beginSearch(new APIRequest<>(mHeader, data)).enqueue(callback);
    }

    public Call<APIResponse<SearchResultResponseData>> fetchResults(SearchResultRequestData data) {
        return mService.fetchResults(new APIRequest<>(mHeader, data));
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
