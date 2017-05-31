package com.fixit.core.rest.services;

import com.fixit.core.rest.apis.OrderServiceAPI;
import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.data.TradesmenOrderRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.TradesmenOrderResponseData;

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
