package com.fixit.controllers;

import android.content.Context;

import com.fixit.FixItApplication;
import com.fixit.app.R;
import com.fixit.data.JobLocation;
import com.fixit.data.MutableLatLng;
import com.fixit.data.Profession;
import com.fixit.database.ProfessionDAO;
import com.fixit.general.SearchManager;
import com.fixit.ui.activities.SearchActivity;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.fixit.validation.AddressValidator;

/**
 * Created by konstantin on 3/29/2017.
 */

public class SearchController extends OrderController {

    private final SearchManager mSearchManager;
    private final ProfessionDAO mProfessionDao;
    private final AddressValidator mAddressValidator;

    public SearchController(FixItApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);
        mSearchManager = new SearchManager(getServerApiFactory().createSearchServiceApi());
        mProfessionDao = getDaoFactory().createProfessionDao();
        mAddressValidator = new AddressValidator();
    }

    public Profession[] getProfessions() {
        return mProfessionDao.findAll();
    }

    public Profession getProfession(String name) {
        return mProfessionDao.findProfessionByName(name);
    }

    public void sendSearch(Context context, Profession profession, JobLocation location, SearchManager.SearchCallback callback) {
        mSearchManager.sendSearch(context, profession, location, callback);
    }

    public void performSearch(final Context context, String professionName, String address, final SearchCallback callback) {
        final Profession profession = getProfession(professionName);
        if(profession != null) {
            mAddressValidator.validate(address, new AddressValidator.AddressValidationCallback() {
                @Override
                public void onAddressValidated(AddressValidator.AddressValidationResult result) {
                    if(result.jobLocation != null) {
                        callback.onAddressValidated();
                        JobLocation jobLocation = result.jobLocation;
                        sendSearch(context, profession, jobLocation, callback);
                        getAnalyticsManager().trackSearch(profession.getName(), address);
                    } else {
                        callback.invalidAddress();
                    }
                }

                @Override
                public void onValidationError(String error, Throwable t) {
                    FILog.e(Constants.LOG_TAG_SEARCH, "Address Geocode Validation error: " + error, t, getApplicationContext());
                    callback.invalidAddress();
                }
            });
        } else {
            callback.invalidProfession();
        }
    }

    public interface SearchCallback extends SearchManager.SearchCallback {
        void invalidProfession();
        void onAddressValidated();
    }

}
