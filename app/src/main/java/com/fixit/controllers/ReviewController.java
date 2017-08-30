package com.fixit.controllers;

import com.fixit.BaseApplication;
import com.fixit.data.Review;
import com.fixit.data.Tradesman;
import com.fixit.general.UnexpectedErrorCallback;
import com.fixit.rest.apis.ReviewDataAPI;
import com.fixit.rest.callbacks.EmptyCallback;
import com.fixit.rest.callbacks.RetryingCallback;
import com.fixit.rest.queries.DataQueryCriteria;
import com.fixit.rest.queries.DataQueryRestrictions;
import com.fixit.utils.GlobalPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by konstantin on 7/23/2017.
 */

public class ReviewController extends TradesmenController {

    private ReviewDataAPI mReviewDataApi;

    public ReviewController(BaseApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);

        mReviewDataApi = getServerApiFactory().createReviewApi();
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

    public void saveReview(Review review) {
        mReviewDataApi.create(review).enqueue(new EmptyCallback<Review>());
    }

    public void updateReview(Review review) {
        mReviewDataApi.update(review).enqueue(new EmptyCallback<Review>());
    }

    public interface LoadReviewsCallback extends UnexpectedErrorCallback {
        void onReviewsLoaded(List<Review> reviews);
    }

}
