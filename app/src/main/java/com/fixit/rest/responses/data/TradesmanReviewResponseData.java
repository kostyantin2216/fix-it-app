package com.fixit.rest.responses.data;

import com.fixit.data.Review;

import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 5/7/2017.
 */

public class TradesmanReviewResponseData {

    private Map<String, Map<String, String>> reviewerDataMappings;
    private List<Review> reviews;

    public Map<String, Map<String, String>> getReviewerDataMappings() {
        return reviewerDataMappings;
    }

    public void setReviewerDataMappings(Map<String, Map<String, String>> reviewerDataMappings) {
        this.reviewerDataMappings = reviewerDataMappings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "TradesmanReviewResponseData{" +
                "reviewerDataMappings=" + reviewerDataMappings +
                ", reviews=" + reviews +
                '}';
    }
}
