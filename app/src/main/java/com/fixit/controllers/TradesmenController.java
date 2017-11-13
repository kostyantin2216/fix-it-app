package com.fixit.controllers;

import com.fixit.FixxitApplication;
import com.fixit.data.Profession;
import com.fixit.data.ReviewData;
import com.fixit.database.ProfessionDAO;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;

/**
 * Created by konstantin on 4/27/2017.
 */

public class TradesmenController extends ReviewController {

    private final ProfessionDAO mProfessionDao;

    public TradesmenController(FixxitApplication baseApplication, UiCallback uiCallback) {
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
