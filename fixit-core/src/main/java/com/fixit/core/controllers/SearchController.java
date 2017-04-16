package com.fixit.core.controllers;

import android.content.Context;

import com.fixit.core.BaseApplication;
import com.fixit.core.data.MutableLatLng;
import com.fixit.core.data.Profession;
import com.fixit.core.database.ProfessionDAO;
import com.fixit.core.general.SearchManager;

import java.util.List;

/**
 * Created by konstantin on 3/29/2017.
 */

public class SearchController extends BaseController {

    private final SearchManager mSearchManager;
    private final ProfessionDAO mProfessionDao;

    public SearchController(BaseApplication baseApplication) {
        super(baseApplication);
        mSearchManager = new SearchManager(getServerApiFactory().createSearchServiceApi());
        mProfessionDao = getDaoFactory().createProfessionDao();
    }

    public List<Profession> getProfessions() {
        return mProfessionDao.findAll();
    }

    public Profession getProfession(String name) {
        return mProfessionDao.findProfessionByName(name);
    }

    public void sendSearch(Context context, Profession profession, MutableLatLng location, SearchManager.SearchCallback callback) {
        mSearchManager.sendSearch(context, profession, location, callback);
    }

}
