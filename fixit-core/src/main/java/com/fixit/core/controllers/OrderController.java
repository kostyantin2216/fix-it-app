package com.fixit.core.controllers;

import com.fixit.core.BaseApplication;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.JobReason;
import com.fixit.core.data.Order;
import com.fixit.core.data.Profession;
import com.fixit.core.data.Tradesman;
import com.fixit.core.database.OrderDAO;
import com.fixit.core.factories.APIFactory;
import com.fixit.core.general.UnexpectedErrorCallback;
import com.fixit.core.rest.apis.JobReasonDataAPI;
import com.fixit.core.rest.apis.OrderServiceAPI;
import com.fixit.core.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.core.rest.callbacks.ManagedServiceCallback;
import com.fixit.core.rest.callbacks.RetryingCallback;
import com.fixit.core.rest.requests.data.TradesmenOrderRequestData;
import com.fixit.core.rest.responses.data.TradesmenOrderResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by konstantin on 5/16/2017.
 */
public class OrderController extends BaseController {

    private final OrderDAO mOrderDao;
    private final OrderServiceAPI mOrderApi;
    private final JobReasonDataAPI mJobReasonApi;

    public OrderController(BaseApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);
        APIFactory apiFactory = getServerApiFactory();
        mOrderDao = getDaoFactory().createOrderDao();
        mOrderApi = apiFactory.createOrderServceApi();
        mJobReasonApi = apiFactory.createJobReasonApi();
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

    public void findReasonsForProfession(final int professionId, final JobReasonsCallback callback) {
        mJobReasonApi.findForProfession(professionId).enqueue(new RetryingCallback<List<JobReason>>(getApplicationContext()) {
            @Override
            public void onRetryFailure(Call<List<JobReason>> call, Throwable t) {
                callback.onUnexpectedErrorOccurred("error while finding job reasons for profession " + professionId, t);
            }

            @Override
            public void onResponse(Call<List<JobReason>> call, Response<List<JobReason>> response) {
                callback.onReceiveJobReasons(response.body());
            }
        });
    }

    public Order saveOrder(JobLocation location, Profession profession, Tradesman[] tradesmen, JobReason[] jobReasons) {
        Order order = Order.newOrder(location, profession, tradesmen, jobReasons);
        order.setId(mOrderDao.insert(order));
        return order;
    }

    public Order getOrder(long id) {
        return mOrderDao.findById(id);
    }

    public interface JobReasonsCallback extends UnexpectedErrorCallback {
        void onReceiveJobReasons(List<JobReason> reasons);
    }

    public interface TradesmenOrderCallback extends GeneralServiceErrorCallback {
        void onOrderComplete(boolean success);
    }

}
