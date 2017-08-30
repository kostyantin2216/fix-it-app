package com.fixit.rest.apis;

import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.APIRequestHeader;
import com.fixit.rest.requests.data.TradesmanReviewsRequestData;
import com.fixit.rest.requests.data.TradesmenRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.TradesmanReviewResponseData;
import com.fixit.rest.responses.data.TradesmenResponseData;
import com.fixit.rest.services.DataService;

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

    public void getTradesmen(String[] tradesmen, Callback<APIResponse<TradesmenResponseData>> callback) {
        mService.getTradesmen(new APIRequest<>(mHeader, new TradesmenRequestData(tradesmen))).enqueue(callback);
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
