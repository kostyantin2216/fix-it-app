package com.fixit.core.rest.apis;

import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.requests.data.TradesmanReviewsRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.TradesmanReviewResponseData;
import com.fixit.core.rest.services.DataService;

import retrofit2.Callback;

/**
 * Created by konstantin on 5/7/2017.
 */

public class DataServiceAPI extends BaseServiceAPI {

    public final static String API_NAME = "services/data";

    private DataService mService;

    public DataServiceAPI(APIRequestHeader header, DataService service) {
        super(header);
        this.mService = service;
    }

    public void getReviewsForTradesman(TradesmanReviewsRequestData requestData, Callback<APIResponse<TradesmanReviewResponseData>> callback) {
        mService.getReviewsForTradesman(new APIRequest<>(mHeader, requestData)).enqueue(callback);
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
