package com.fixit.controllers;

import com.fixit.FixxitApplication;
import com.fixit.data.Review;
import com.fixit.data.ReviewData;
import com.fixit.data.Tradesman;
import com.fixit.general.UnexpectedErrorCallback;
import com.fixit.rest.apis.DataServiceAPI;
import com.fixit.rest.apis.ReviewDataAPI;
import com.fixit.rest.callbacks.EmptyCallback;
import com.fixit.rest.callbacks.ManagedServiceCallback;
import com.fixit.rest.callbacks.RetryingCallback;
import com.fixit.rest.queries.DataQueryCriteria;
import com.fixit.rest.queries.DataQueryRestrictions;
import com.fixit.rest.requests.data.TradesmanReviewsRequestData;
import com.fixit.rest.responses.data.TradesmanReviewResponseData;
import com.fixit.utils.Constants;
import com.fixit.utils.GlobalPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by konstantin on 7/23/2017.
 */

public class ReviewController extends BaseController {

    private final DataServiceAPI mDataApi;
    private final ReviewDataAPI mReviewDataApi;

    public ReviewController(FixxitApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);

        mDataApi = baseApplication.getServerApiFactory().createDataServiceApi();
        mReviewDataApi = getServerApiFactory().createReviewApi();
    }

    public void loadReviews(String tradesmanId, final TradesmenController.TradesmanReviewsCallback callback) {
        mDataApi.getReviewsForTradesman(new TradesmanReviewsRequestData(tradesmanId),
                new ManagedServiceCallback<TradesmanReviewResponseData>(getApplicationContext(), callback, "Error while fetching reviews for tradesman " + tradesmanId) {
                    @Override
                    public void onResponse(TradesmanReviewResponseData responseData) {
                        callback.onReviewsLoaded(parseReviews(responseData));
                    }
                });
    }

    public void loadReviewForTradesmanByUser(Tradesman tradesman, final LoadReviewsCallback callback) {
        String userId = GlobalPreferences.getUserId(getApplicationContext());
        Call<List<Review>> call = mReviewDataApi.query(DataQueryCriteria.create()
                .add(DataQueryRestrictions.eq("tradesmanId", tradesman.get_id()))
                .add(DataQueryRestrictions.eq("userId", userId))
        );
        call.enqueue(new RetryingCallback<List<Review>>(getApplicationContext()) {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                callback.onReviewsLoaded(response.body());
            }

            @Override
            public void onRetryFailure(Call<List<Review>> call, Throwable t) {
                callback.onUnexpectedErrorOccurred("Could not load reviews", t);
            }
        });
    }

    public void saveReview(Tradesman tradesman, Review review) {
        getAnalyticsManager().trackTradesmanReview(review.getRating(), review.getTradesmanId(), tradesman.getCompanyName());
        mReviewDataApi.create(review).enqueue(new EmptyCallback<>());
    }

    public void updateReview(Review review) {
        mReviewDataApi.update(review).enqueue(new EmptyCallback<>());
    }

    public ReviewData[] parseReviews(TradesmanReviewResponseData responseData) {
        List<Review> reviews = responseData.getReviews();

        int reviewCount = reviews.size();
        if(reviewCount > 0) {
            Set<ReviewData> reviewData = new HashSet<>();
            Map<String, Map<String, String>> reviewerDataMapping = responseData.getReviewerDataMappings();

            for (int i = 0; i < reviewCount; i++) {
                Review review = reviews.get(i);
                Map<String, String> reviewerData = reviewerDataMapping.get(review.getUserId());

                if(reviewerData !=  null) {
                    String userAvatar = reviewerData.get(Constants.KEY_USER_AVATAR);
                    String userName = reviewerData.get(Constants.KEY_USER_NAME);
                    reviewData.add(new ReviewData(review, userName, userAvatar));
                }
            }

            return reviewData.toArray(new ReviewData[reviewData.size()]);
        }

        return new ReviewData[0];
    }

    public interface LoadReviewsCallback extends UnexpectedErrorCallback {
        void onReviewsLoaded(List<Review> reviews);
    }

}
