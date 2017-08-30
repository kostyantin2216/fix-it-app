package com.fixit.data;

/**
 * Created by konstantin on 5/7/2017.
 */

public class ReviewData {
    public final Review review;
    public final String reviewerName;
    public final String reviewerAvatar;

    public ReviewData(Review review, String reviewerName, String reviewerAvatar) {
        this.review = review;
        this.reviewerName = reviewerName;
        this.reviewerAvatar = reviewerAvatar;
    }
}
