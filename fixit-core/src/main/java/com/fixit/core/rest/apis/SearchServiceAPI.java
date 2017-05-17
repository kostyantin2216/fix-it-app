package com.fixit.core.rest.apis;

import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.requests.data.SearchRequestData;
import com.fixit.core.rest.requests.data.SearchResultRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.SearchResponseData;
import com.fixit.core.rest.responses.data.SearchResultResponseData;
import com.fixit.core.rest.services.SearchService;

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
