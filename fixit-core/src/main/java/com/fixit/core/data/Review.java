package com.fixit.core.data;

import java.util.Date;

/**
 * Created by konstantin on 5/1/2017.
 */

public class Review {

    private String userId;
    private String tradesmanId;
    private String title;
    private String content;
    private float rating;
    private Date createdAt;

    public Review() { }

    public Review(String userId, String tradesmanId, String title, String content, float rating, Date createdAt) {
        this.userId = userId;
        this.tradesmanId = tradesmanId;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTradesmanId() {
        return tradesmanId;
    }

    public void setTradesmanId(String tradesmanId) {
        this.tradesmanId = tradesmanId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "userId='" + userId + '\'' +
                ", tradesmanId='" + tradesmanId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                '}';
    }
}
