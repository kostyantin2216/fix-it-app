package com.fixit.rest.services;

import com.fixit.rest.apis.DataServiceAPI;
import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.data.TradesmanReviewsRequestData;
import com.fixit.rest.requests.data.TradesmenRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.TradesmanReviewResponseData;
import com.fixit.rest.responses.data.TradesmenResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by konstantin on 5/7/2017.
 */

public interface DataService {

    @POST(DataServiceAPI.API_NAME + "/reviewsForTradesman")
    Call<APIResponse<TradesmanReviewResponseData>> getReviewsForTradesman(@Body APIRequest<TradesmanReviewsRequestData> request);

    @POST(DataServiceAPI.API_NAME + "/tradesmen")
    Call<APIResponse<TradesmenResponseData>> getTradesmen(@Body APIRequest<TradesmenRequestData> request);


}
