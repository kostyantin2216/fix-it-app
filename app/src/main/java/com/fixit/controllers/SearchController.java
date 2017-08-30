package com.fixit.controllers;

import android.content.Context;

import com.fixit.BaseApplication;
import com.fixit.data.MutableLatLng;
import com.fixit.data.Profession;
import com.fixit.database.ProfessionDAO;
import com.fixit.general.SearchManager;

/**
 * Created by konstantin on 3/29/2017.
 */

public class SearchController extends OrderController {

    private final SearchManager mSearchManager;
    private final ProfessionDAO mProfessionDao;

    public SearchController(BaseApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);
        mSearchManager = new SearchManager(getServerApiFactory().createSearchServiceApi());
        mProfessionDao = getDaoFactory().createProfessionDao();
    }

    public Profession[] getProfessions() {
        return mProfessionDao.findAll();
    }

    public Profession getProfession(String name) {
        return mProfessionDao.findProfessionByName(name);
    }

    public void sendSearch(Context context, Profession profession, MutableLatLng location, SearchManager.SearchCallback callback) {
        mSearchManager.sendSearch(context, profession, location, callback);
    }

}