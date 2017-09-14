package com.fixit.controllers;

import com.fixit.FixItApplication;
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

public class TradesmenController extends ReviewController {

    private final ProfessionDAO mProfessionDao;

    public TradesmenController(FixItApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);

        mProfessionDao = baseApplication.getDaoFactory().createProfessionDao();
    }

    public Profession getProfession(long id) {
        return mProfessionDao.findById(id);
    }

    public interface TradesmanReviewsCallback extends GeneralServiceErrorCallback {
        void onReviewsLoaded(ReviewData[] reviewData);
    }

}
