package com.fixit.core.rest.requests.data;

import com.fixit.core.data.JobLocation;
import com.fixit.core.data.Tradesman;

import java.util.Arrays;

/**
 * Created by Kostyantin on 5/29/2017.
 */
public class TradesmenOrderRequestData {

    private JobLocation jobLocation;
    private Tradesman[] tradesmen;
    private String reason;

    public TradesmenOrderRequestData(Tradesman[] tradesmen, JobLocation jobLocation, String reason) {
        this.jobLocation = jobLocation;
        this.tradesmen = tradesmen;
        this.reason = reason;
    }

    public JobLocation getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(JobLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    public Tradesman[] getTradesmen() {
        return tradesmen;
    }

    public void setTradesmen(Tradesman[] tradesmen) {
        this.tradesmen = tradesmen;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "TradesmenOrderRequestData{" +
                "jobLocation=" + jobLocation +
                ", tradesmen=" + Arrays.toString(tradesmen) +
                ", reason='" + reason + '\'' +
                '}';
    }
}
