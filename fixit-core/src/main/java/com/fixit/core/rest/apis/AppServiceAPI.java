package com.fixit.core.rest.apis;

import com.fixit.core.rest.ServerAPI;
import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.requests.data.SearchRequestData;
import com.fixit.core.rest.requests.data.SearchResultRequestData;
import com.fixit.core.rest.requests.data.SynchronizationRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.APIResponseHeader;
import com.fixit.core.rest.responses.data.SearchResponseData;
import com.fixit.core.rest.responses.data.SearchResultResponseData;
import com.fixit.core.rest.responses.data.SynchronizationResponseData;
import com.fixit.core.rest.services.AppServiceService;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Kostyantin on 3/20/2017.
 */

public class AppServiceAPI implements ServerAPI {

    public final static String API_NAME = "services/app";

    public final static String EP_SYNCHRONIZE = "/synchronize";
    public final static String EP_SEARCH = "/startTradesmanSearch";
    public final static String EP_RESULTS = "/fetchTradesmenResults";

    private final AppServiceService service;
    private final APIRequestHeader header;

    public AppServiceAPI(APIRequestHeader header, AppServiceService service) {
        this.service = service;
        this.header = header;
    }

    public Call<APIResponse<SynchronizationResponseData>> synchronize(SynchronizationRequestData data) {
        return service.synchronize(new APIRequest<>(header, data));
    }

    public void beginSearch(SearchRequestData data, Callback<APIResponse<SearchResponseData>> callback) {
        service.beginSearch(new APIRequest<>(header, data)).enqueue(callback);
    }

    public Call<APIResponse<SearchResultResponseData>> fetchResults(SearchResultRequestData data) {
        return service.fetchResults(new APIRequest<>(header, data));
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }

}
