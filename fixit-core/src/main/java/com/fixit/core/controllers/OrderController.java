package com.fixit.core.controllers;

import com.fixit.core.BaseApplication;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.Tradesman;
import com.fixit.core.rest.apis.OrderServiceAPI;
import com.fixit.core.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.core.rest.callbacks.ManagedServiceCallback;
import com.fixit.core.rest.requests.data.TradesmenOrderRequestData;
import com.fixit.core.rest.responses.data.TradesmenOrderResponseData;

/**
 * Created by konstantin on 5/16/2017.
 */
public class OrderController extends BaseController {

    private final OrderServiceAPI mOrderApi;

    public OrderController(BaseApplication baseApplication) {
        super(baseApplication);
        mOrderApi = getServerApiFactory().createOrderServceApi();
    }

    public void orderTradesmen(Tradesman[] tradesmen, JobLocation location, String reason, final TradesmenOrderCallback callback) {
        TradesmenOrderRequestData requestData = new TradesmenOrderRequestData(tradesmen, location, reason);
        mOrderApi.orderTradesmen(requestData).enqueue(new ManagedServiceCallback<TradesmenOrderResponseData>(getApplicationContext(), callback, "Unexpected error while trying to order tradesmen") {
            @Override
            public void onResponse(TradesmenOrderResponseData responseData) {
                callback.onOrderComplete(responseData.isComplete());
            }
        });
    }

    public interface TradesmenOrderCallback extends GeneralServiceErrorCallback {
        void onOrderComplete(boolean success);
    }

}
