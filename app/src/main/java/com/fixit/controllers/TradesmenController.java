package com.fixit.controllers;

import com.fixit.BaseApplication;
import com.fixit.data.Profession;
import com.fixit.data.Review;
import com.fixit.data.ReviewData;
import com.fixit.database.ProfessionDAO;
import com.fixit.rest.apis.DataServiceAPI;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.rest.callbacks.ManagedServiceCallback;
import com.fixit.rest.requests.data.TradesmanReviewsRequestData;
import com.fixit.rest.responses.data.TradesmanReviewResponseData;
import com.fixit.utils.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 4/27/2017.
 */

public class TradesmenController extends BaseController {

    private final DataServiceAPI mDataApi;
    private final ProfessionDAO mProfessionDao;

    public TradesmenController(BaseApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);

        mDataApi = baseApplication.getServerApiFactory().createDataServiceApi();
        mProfessionDao = baseApplication.getDaoFactory().createProfessionDao();
    }

    public void getReviews(String tradesmanId, final TradesmanReviewsCallback callback) {
        mDataApi.getReviewsForTradesman(new TradesmanReviewsRequestData(tradesmanId),
                new ManagedServiceCallback<TradesmanReviewResponseData>(getApplicationContext(), callback, "Error while fetching reviews for tradesman " + tradesmanId) {
                    @Override
                    public void onResponse(TradesmanReviewResponseData responseData) {
                        callback.onReviewsLoaded(parseReviews(responseData));
                    }
        });
    }

    public Profession getProfession(long id) {
        return mProfessionDao.findById(id);
    }

    private ReviewData[] parseReviews(TradesmanReviewResponseData responseData) {
        List<Review> reviews = responseData.getReviews();

        int reviewCount = reviews.size();
        if(reviewCount > 0) {
            ReviewData[] data = new ReviewData[reviews.size()];
            Map<String, Map<String, String>> reviewerDataMapping = responseData.getReviewerDataMappings();

            for (int i = 0; i < reviewCount; i++) {
                Review review = reviews.get(i);
                Map<String, String> reviewerData = reviewerDataMapping.get(review.getUserId());

                String userAvatar = reviewerData.get(Constants.KEY_USER_AVATAR);
                String userName = reviewerData.get(Constants.KEY_USER_NAME);
                data[i] = new ReviewData(review, userName, userAvatar);
            }

            return data;
        }

        return new ReviewData[0];
    }

    public interface TradesmanReviewsCallback extends GeneralServiceErrorCallback {
        void onReviewsLoaded(ReviewData[] reviewData);
    }

}
