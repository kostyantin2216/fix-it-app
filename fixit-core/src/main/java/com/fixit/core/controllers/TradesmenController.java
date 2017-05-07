package com.fixit.core.controllers;

import android.text.TextUtils;

import com.fixit.core.BaseApplication;
import com.fixit.core.data.Review;
import com.fixit.core.data.ReviewData;
import com.fixit.core.factories.DAOFactory;
import com.fixit.core.factories.ServerAPIFactory;
import com.fixit.core.general.UnexpectedErrorCallback;
import com.fixit.core.rest.APIError;
import com.fixit.core.rest.apis.DataServiceAPI;
import com.fixit.core.rest.callbacks.AppServiceCallback;
import com.fixit.core.rest.callbacks.AppServiceErrorCallback;
import com.fixit.core.rest.requests.data.TradesmanReviewsRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.TradesmanReviewResponseData;
import com.fixit.core.utils.CommonUtils;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.FILog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by konstantin on 4/27/2017.
 */

public class TradesmenController extends BaseController {

    private final DataServiceAPI mDataApi;

    public TradesmenController(BaseApplication baseApplication) {
        super(baseApplication);

        mDataApi = baseApplication.getServerApiFactory().createDataServiceApi();
    }

    public void getReviews(final String tradesmanId, final TradesmanReviewsCallback callback) {
        mDataApi.getReviewsForTradesman(new TradesmanReviewsRequestData(tradesmanId)).enqueue(new AppServiceCallback<TradesmanReviewResponseData>(getApplicationContext()) {
            @Override
            public void onResponse(TradesmanReviewResponseData responseData) {
                callback.onReviewsLoaded(parseReviews(responseData));
            }

            @Override
            public void onAppServiceError(List<APIError> errors) {
                callback.onAppServiceError(errors);
            }

            @Override
            public void onRetryFailure(Call<APIResponse<TradesmanReviewResponseData>> call, Throwable t) {
                callback.onUnexpectedErrorOccurred("Error while fetching reviews for tradesman " + tradesmanId, t);
            }
        });
    }

    private List<ReviewData> parseReviews(TradesmanReviewResponseData responseData) {
        List<ReviewData> dataList = new ArrayList<>();

        List<Review> reviews = responseData.getReviews();
        if(CommonUtils.notEmpty(reviews)) {
            Map<String, Map<String, String>> reviewerDataMapping = responseData.getReviewerDataMappings();
            for (Review review : responseData.getReviews()) {
                Map<String, String> reviewerData = reviewerDataMapping.get(review.getUserId());
                if (reviewerData != null) {
                    String userAvatar = reviewerData.get(Constants.KEY_USER_AVATAR);
                    String userName = reviewerData.get(Constants.KEY_USER_NAME);
                    if (!TextUtils.isEmpty(userAvatar) && !TextUtils.isEmpty(userName)) {
                        dataList.add(new ReviewData(review, userName, userAvatar));
                    } else {
                        FILog.w("user " + review.getUserId() + " is missing userName or userAvatar while parsing reviews");
                    }
                } else {
                    FILog.w("could not find data for user " + review.getUserId() + " while parsing reviews");
                }
            }
        }

        return dataList;
    }


    public interface TradesmanReviewsCallback extends UnexpectedErrorCallback, AppServiceErrorCallback {
        void onReviewsLoaded(List<ReviewData> reviewData);
    }

}
