package com.fixit.core.data;

import java.util.Date;

/**
 * Created by konstantin on 4/3/2017.
 */

public class TradesmanReview {

    private String tradesmanId;
    private String userId;
    private String title;
    private String content;
    private float rating;
    private boolean onDisplay;
    private Date createdAt;

    public String getTradesmanId() {
        return tradesmanId;
    }

    public void setTradesmanId(String tradesmanId) {
        this.tradesmanId = tradesmanId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Boolean isOnDisplay() {
        return onDisplay;
    }

    public void setOnDisplay(Boolean onDisplay) {
        onDisplay = onDisplay;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TradesmanReview{" +
                "tradesmanId='" + tradesmanId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", onDisplay=" + onDisplay +
                ", createdAt=" + createdAt +
                '}';
    }
}
