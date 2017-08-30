package com.fixit.rest.apis;

import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.APIRequestHeader;
import com.fixit.rest.requests.data.TradesmenOrderRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.TradesmenOrderResponseData;
import com.fixit.rest.services.OrderService;

import retrofit2.Call;

/**
 * Created by Kostyantin on 5/29/2017.
 */

public class OrderServiceAPI extends BaseServiceAPI {

    public final static String API_NAME = "services/orders";

    private final OrderService mService;

    public OrderServiceAPI(APIRequestHeader header, OrderService orderService) {
        super(header);
        mService = orderService;
    }

    public Call<APIResponse<TradesmenOrderResponseData>> orderTradesmen(TradesmenOrderRequestData requestData) {
        return mService.orderTradesmen(new APIRequest<>(mHeader, requestData));
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
