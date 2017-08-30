package com.fixit.rest.services;

import com.fixit.rest.apis.OrderServiceAPI;
import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.data.TradesmenOrderRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.TradesmenOrderResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Kostyantin on 5/29/2017.
 */

public interface OrderService {

    @POST(OrderServiceAPI.API_NAME + "/orderTradesmen")
    Call<APIResponse<TradesmenOrderResponseData>> orderTradesmen(@Body APIRequest<TradesmenOrderRequestData> request);

}
