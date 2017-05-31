package com.fixit.core.rest.responses.data;

/**
 * Created by Kostyantin on 5/29/2017.
 */

public class TradesmenOrderResponseData {

    private boolean complete;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "TradesmenOrderResponseData [complete=" + complete + "]";
    }

}
