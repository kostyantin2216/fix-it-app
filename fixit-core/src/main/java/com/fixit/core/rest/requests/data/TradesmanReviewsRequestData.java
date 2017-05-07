package com.fixit.core.rest.requests.data;

/**
 * Created by konstantin on 5/7/2017.
 */

public class TradesmanReviewsRequestData {

    private String tradesmanId;

    public TradesmanReviewsRequestData() { }

    public TradesmanReviewsRequestData(String tradesmanId) {
        this.tradesmanId = tradesmanId;
    }

    public String getTradesmanId() {
        return tradesmanId;
    }

    public void setTradesmanId(String tradesmanId) {
        this.tradesmanId = tradesmanId;
    }

    @Override
    public String toString() {
        return "TradesmanReviewsRequestData{" +
                "tradesmanId='" + tradesmanId + '\'' +
                '}';
    }
}
