package com.fixit.controllers;

import com.fixit.FixItApplication;
import com.fixit.data.JobLocation;
import com.fixit.data.JobReason;
import com.fixit.data.Order;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.data.Tradesman;
import com.fixit.database.JobReasonDAO;
import com.fixit.database.OrderDataDAO;
import com.fixit.factories.APIFactory;
import com.fixit.factories.DAOFactory;
import com.fixit.factories.OrderFactory;
import com.fixit.general.UnexpectedErrorCallback;
import com.fixit.rest.APIError;
import com.fixit.rest.apis.JobReasonDataAPI;
import com.fixit.rest.apis.OrderServiceAPI;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.rest.callbacks.ManagedServiceCallback;
import com.fixit.rest.callbacks.RetryingCallback;
import com.fixit.rest.requests.data.TradesmenOrderRequestData;
import com.fixit.rest.responses.data.TradesmenOrderResponseData;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.fixit.utils.GlobalPreferences;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by konstantin on 5/16/2017.
 */
public class OrderController extends TradesmenController {

    private final OrderFactory mOrderFactory;
    private final OrderDataDAO mOrderDataDao;
    private final OrderServiceAPI mOrderApi;
    private final JobReasonDAO mJobReasonDao;

    public OrderController(FixItApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);
        APIFactory apiFactory = getServerApiFactory();
        DAOFactory daoFactory = getDaoFactory();
        mOrderDataDao = daoFactory.createOrderDataDao();
        mOrderApi = apiFactory.createOrderServiceApi();
        mJobReasonDao = daoFactory.createJobReasonDao();

        mOrderFactory = new OrderFactory(
                getAppCache().getTradesmanCache(),
                daoFactory.createProfessionDao(),
                daoFactory.createJobReasonDao()
        );
    }

    public void orderTradesmen(final Tradesman[] tradesmen, JobLocation location, JobReason[] jobReasons, String comment, final TradesmenOrderCallback callback) {
        TradesmenOrderRequestData requestData = new TradesmenOrderRequestData(tradesmen, jobReasons, location, comment);
        mOrderApi.orderTradesmen(requestData).enqueue(new ManagedServiceCallback<TradesmenOrderResponseData>(getApplicationContext(), callback, "Unexpected error while trying to order tradesmen") {
            @Override
            public void onResponse(TradesmenOrderResponseData responseData) {
                OrderData orderData = responseData.getOrderData();
                getAppCache().getTradesmanCache().put(tradesmen);
                saveOrder(orderData);
                callback.onOrderComplete(orderData);
            }
        });
    }

    public void findReasonsForProfession(final long professionId, final JobReasonsCallback callback) {
        callback.onReceiveJobReasons(mJobReasonDao.findByProperty(JobReasonDAO.KEY_PROFESSION_ID, String.valueOf(professionId)));
    }

    public void saveOrder(OrderData orderData) {
        mOrderDataDao.insert(orderData);
    }

    public void saveOrders(OrderData[] orderData) {
        mOrderDataDao.insert(Arrays.asList(orderData));
    }

    public Order orderCompleted(String orderId, JobLocation location, Profession profession, Tradesman[] tradesmen, JobReason[] jobReasons, String comment, Date createdAt) {
        Order order = new Order(orderId, location, profession, tradesmen, jobReasons, comment, createdAt);
        GlobalPreferences.setLastOrderId(getApplicationContext(), orderId);
        return order;
    }

    public OrderData getOrder(String id) {
        return mOrderDataDao.findOneByProperty(OrderDataDAO.KEY_ID, id);
    }

    public OrderData getLatestOrder() {
        String lastOrderId = GlobalPreferences.getLastOrderId(getApplicationContext());

        if(lastOrderId != null) {
            return mOrderDataDao.findOneByProperty(OrderDataDAO.KEY_ID, lastOrderId);
        } else {
            return null;
        }
    }

    public void loadLatestOrder(SingleOrderCallback orderCallback) {
        OrderData orderData = getLatestOrder();

        if(orderData == null) {
            orderCallback.onOrderLoaded(null);
        } else {
           loadOrderHistory(orderData, orderCallback);
        }
    }

    public void loadOrderHistory(String orderId, SingleOrderCallback orderCallback) {
        OrderData orderData = getOrder(orderId);

        if(orderData == null) {
            orderCallback.onOrderLoaded(null);
        } else {
            loadOrderHistory(orderData, orderCallback);
        }
    }

    public void loadOrderHistory(OrderData orderData, final SingleOrderCallback orderCallback) {
        mOrderFactory.createOrders(getApplicationContext(), new OrderData[]{orderData}, new OrderFactory.OrderFactoryCallback() {
            @Override
            public void onOrdersCreated(Order[] orders) {
                orderCallback.onOrderLoaded(orders[0]);
            }

            @Override
            public void onUnexpectedErrorOccurred(String msg, Throwable t) {
                orderCallback.onUnexpectedErrorOccurred(msg, t);
            }

            @Override
            public void onAppServiceError(List<APIError> errors) {
                orderCallback.onAppServiceError(errors);
            }

            @Override
            public void onServerError() {
                orderCallback.onServerError();
            }
        });
    }

    public void loadOrderHistory(final OrderCallback callback) {
        OrderData[] orderHistory = mOrderDataDao.findAll();

        if(orderHistory.length == 0) {
            FILog.i(Constants.LOG_TAG_ORDER_HISTORY, "could not find any order history");
            callback.onOrdersLoaded(new Order[0]);
        } else {
            mOrderFactory.createOrders(getApplicationContext(), orderHistory, new OrderFactory.OrderFactoryCallback() {
                @Override
                public void onOrdersCreated(Order[] orders) {
                    callback.onOrdersLoaded(orders);
                }

                @Override
                public void onUnexpectedErrorOccurred(String msg, Throwable t) {
                    callback.onUnexpectedErrorOccurred(msg, t);
                }

                @Override
                public void onAppServiceError(List<APIError> errors) {
                    callback.onAppServiceError(errors);
                }

                @Override
                public void onServerError() {
                    callback.onServerError();
                }
            });
        }
    }

    public void cleanOrderFactory() {
        mOrderFactory.cleanGenerator();
    }

    public interface OrderCallback extends GeneralServiceErrorCallback {
        void onOrdersLoaded(Order[] orders);
    }

    public interface SingleOrderCallback extends GeneralServiceErrorCallback {
        void onOrderLoaded(Order order);
    }

    public interface JobReasonsCallback extends UnexpectedErrorCallback {
        void onReceiveJobReasons(JobReason[] reasons);
    }

    public interface TradesmenOrderCallback extends GeneralServiceErrorCallback {
        void onOrderComplete(OrderData orderData);
    }

}
