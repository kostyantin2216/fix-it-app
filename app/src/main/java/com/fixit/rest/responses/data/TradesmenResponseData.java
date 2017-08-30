package com.fixit.rest.responses.data;

import com.fixit.data.Tradesman;

import java.util.Arrays;

/**
 * Created by Kostyantin on 8/26/2017.
 */

public class TradesmenResponseData {

    private Tradesman[] tradesmen;

    public TradesmenResponseData() { }

    public TradesmenResponseData(Tradesman[] tradesmen) {
        this.tradesmen = tradesmen;
    }

    public Tradesman[] getTradesmen() {
        return tradesmen;
    }

    public void setTradesmen(Tradesman[] tradesmen) {
        this.tradesmen = tradesmen;
    }

    @Override
    public String toString() {
        return "TradesmenResponseData{" +
                "tradesmen=" + Arrays.toString(tradesmen) +
                '}';
    }
}
