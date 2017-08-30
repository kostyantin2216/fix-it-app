package com.fixit.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Kostyantin on 7/16/2017.
 */

public class Order implements DataModelObject {

    public static final Comparator<Order> CREATION_DATE_COMPARATOR = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            return (int) (o2.createdAt.getTime() - o1.createdAt.getTime());
        }
    };

    private String id;
    private JobLocation jobLocation;
    private Profession profession;
    private Tradesman[] tradesmen;
    private JobReason[] jobReasons;
    private String comment;
    private Date createdAt;

    public Order(String id, JobLocation jobLocation, Profession profession, Tradesman[] tradesmen, JobReason[] jobReasons, String comment, Date createdAt) {
        this.id = id;
        this.jobLocation = jobLocation;
        this.profession = profession;
        this.tradesmen = tradesmen;
        this.jobReasons = jobReasons;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JobLocation getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(JobLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", jobLocation=" + jobLocation +
                ", profession=" + profession +
                ", tradesmen=" + Arrays.toString(tradesmen) +
                ", jobReasons=" + Arrays.toString(jobReasons) +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
