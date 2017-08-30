package com.fixit.rest.responses.data;

import com.fixit.data.Tradesman;

import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 4/3/2017.
 */

public class SearchResultResponseData {

    private boolean complete;
    private List<Tradesman> tradesmen;
    private Map<String, Integer> reviewCountForTradesmen;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public List<Tradesman> getTradesmen() {
        return tradesmen;
    }

    public void setTradesmen(List<Tradesman> tradesmen) {
        this.tradesmen = tradesmen;
    }

    public Map<String, Integer> getReviewCountForTradesmen() {
        return reviewCountForTradesmen;
    }

    public void setReviewCountForTradesmen(Map<String, Integer> reviewCountForTradesmen) {
        this.reviewCountForTradesmen = reviewCountForTradesmen;
    }

    @Override
    public String toString() {
        return "SearchResultResponseData{" +
                "complete=" + complete +
                ", tradesmen=" + tradesmen +
                ", reviewCountForTradesmen=" + reviewCountForTradesmen +
                '}';
    }
}
