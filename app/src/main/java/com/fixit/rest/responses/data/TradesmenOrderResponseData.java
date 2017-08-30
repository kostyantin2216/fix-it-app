package com.fixit.rest.responses.data;

import com.fixit.data.OrderData;

/**
 * Created by Kostyantin on 5/29/2017.
 */

public class TradesmenOrderResponseData {

    private OrderData orderData;

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    @Override
    public String toString() {
        return "TradesmenOrderResponseData{" +
                "orderData=" + orderData +
                '}';
    }
}
