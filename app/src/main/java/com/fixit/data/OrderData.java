package com.fixit.data;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Kostyantin on 8/22/2017.
 */

public class OrderData implements DataModelObject {

    private String id;
    private String[] tradesmen;
    private long professionId;
    private JobLocation location;
    private long[] jobReasons;
    private String comment;
    private boolean feedbackProvided;
    private Date createdAt;

    public OrderData() { }

    public OrderData(String id, String[] tradesmen, long professionId, JobLocation location, long[] jobReasons, String comment, boolean feedbackProvided, Date createdAt) {
        this.id = id;
        this.tradesmen = tradesmen;
        this.professionId = professionId;
        this.location = location;
        this.jobReasons = jobReasons;
        this.comment = comment;
        this.feedbackProvided = feedbackProvided;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getTradesmen() {
        return tradesmen;
    }

    public void setTradesmen(String[] tradesmen) {
        this.tradesmen = tradesmen;
    }

    public long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(long professionId) {
        this.professionId = professionId;
    }

    public JobLocation getLocation() {
        return location;
    }

    public void setLocation(JobLocation location) {
        this.location = location;
    }

    public long[] getJobReasons() {
        return jobReasons;
    }

    public void setJobReasons(long[] jobReasons) {
        this.jobReasons = jobReasons;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isFeedbackProvided() {
        return feedbackProvided;
    }

    public void setFeedbackProvided(boolean feedbackProvided) {
        this.feedbackProvided = feedbackProvided;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "id='" + id + '\'' +
                ", tradesmen=" + Arrays.toString(tradesmen) +
                ", professionId=" + professionId +
                ", location=" + location +
                ", jobReasons=" + Arrays.toString(jobReasons) +
                ", comment='" + comment + '\'' +
                ", feedbackProvided=" + feedbackProvided +
                ", createdAt=" + createdAt +
                '}';
    }
}
