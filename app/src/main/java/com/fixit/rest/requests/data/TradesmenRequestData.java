package com.fixit.rest.requests.data;

import java.util.Arrays;

/**
 * Created by Kostyantin on 8/26/2017.
 */

public class TradesmenRequestData {

    public String[] tradesmenIds;

    public TradesmenRequestData() { }

    public TradesmenRequestData(String[] tradesmenIds) {
        this.tradesmenIds = tradesmenIds;
    }

    public String[] getTradesmenIds() {
        return tradesmenIds;
    }

    public void setTradesmenIds(String[] tradesmenIds) {
        this.tradesmenIds = tradesmenIds;
    }

    @Override
    public String toString() {
        return "TradesmenRequestData{" +
                "tradesmenIds=" + Arrays.toString(tradesmenIds) +
                '}';
    }
}
