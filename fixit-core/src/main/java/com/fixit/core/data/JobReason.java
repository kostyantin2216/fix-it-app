package com.fixit.core.data;

import java.util.Date;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class JobReason {

    private Integer id;
    private Integer professionId;
    private String name;
    private String comment;
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Integer professionId) {
        this.professionId = professionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static String toDescription(JobReason[] jobReasons) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < jobReasons.length; i++) {
            sb.append(jobReasons[i].name);
            if(i < jobReasons.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "JobReason{" +
                "id=" + id +
                ", professionId=" + professionId +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
