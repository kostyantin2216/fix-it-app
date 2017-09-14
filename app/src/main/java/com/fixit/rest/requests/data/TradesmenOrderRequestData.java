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
    private String comment;

    public TradesmenOrderRequestData(Tradesman[] tradesmen, JobReason[] jobReasons, JobLocation jobLocation, String comment) {
        this.jobLocation = jobLocation;
        this.tradesmen = tradesmen;
        this.jobReasons = jobReasons;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "TradesmenOrderRequestData{" +
                "jobLocation=" + jobLocation +
                ", tradesmen=" + Arrays.toString(tradesmen) +
                ", jobReasons=" + Arrays.toString(jobReasons) +
                ", comment='" + comment + '\'' +
                '}';
    }

}
