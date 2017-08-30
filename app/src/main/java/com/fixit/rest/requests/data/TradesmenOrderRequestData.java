package com.fixit.rest.requests.data;

import com.fixit.data.JobLocation;
import com.fixit.data.JobReason;
import com.fixit.data.Tradesman;

import java.util.Arrays;

/**
 * Created by Kostyantin on 5/29/2017.
 */
public class TradesmenOrderRequestData {

    private JobLocation jobLocation;
    private Tradesman[] tradesmen;
    private JobReason[] jobReasons;
    private String reason;

    public TradesmenOrderRequestData(Tradesman[] tradesmen, JobReason[] jobReasons, JobLocation jobLocation, String reason) {
        this.jobLocation = jobLocation;
        this.tradesmen = tradesmen;
        this.jobReasons = jobReasons;
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

    public JobReason[] getJobReasons() {
        return jobReasons;
    }

    public void setJobReasons(JobReason[] jobReasons) {
        this.jobReasons = jobReasons;
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
                ", jobReasons=" + Arrays.toString(jobReasons) +
                ", reason='" + reason + '\'' +
                '}';
    }

}
