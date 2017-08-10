package com.fixit.core.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Kostyantin on 7/16/2017.
 */

public class Order implements DataModelObject {

    public static Order newOrder(JobLocation jobLocation, Profession profession, Tradesman[] tradesmen, JobReason[] jobReasons) {
        return new Order(null, jobLocation, profession, tradesmen, jobReasons, new Date(), false);
    }

    public static final Comparator<Order> CREATION_DATE_COMPARATOR = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            return (int) (o2.creationDate.getTime() - o1.creationDate.getTime());
        }
    };

    private Long id;
    private JobLocation jobLocation;
    private Profession profession;
    private Tradesman[] tradesmen;
    private JobReason[] jobReasons;
    private Date creationDate;
    private boolean complete;

    public Order(Long id, JobLocation jobLocation, Profession profession, Tradesman[] tradesmen, JobReason[] jobReasons, Date creationDate, boolean complete) {
        this.id = id;
        this.jobLocation = jobLocation;
        this.profession = profession;
        this.tradesmen = tradesmen;
        this.jobReasons = jobReasons;
        this.creationDate = creationDate;
        this.complete = complete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", jobLocation=" + jobLocation +
                ", profession=" + profession +
                ", tradesmen=" + Arrays.toString(tradesmen) +
                ", jobReasons=" + Arrays.toString(jobReasons) +
                ", creationDate=" + creationDate +
                ", complete=" + complete +
                '}';
    }
}
