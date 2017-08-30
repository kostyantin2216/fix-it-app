package com.fixit.controllers;

import com.fixit.BaseApplication;
import com.fixit.general.SearchManager;

/**
 * Created by konstantin on 4/3/2017.
 */

public class ResultsController extends TradesmenController {

    private final SearchManager mSearchManager;

    private SearchManager.ResultsFetcher mResultsFetcher;

    public ResultsController(BaseApplication baseApplication, UiCallback uiCallback) {
        super(baseApplication, uiCallback);
        mSearchManager = new SearchManager(getServerApiFactory().createSearchServiceApi());
    }

    public void fetchResults(String searchId, SearchManager.ResultCallback callback) {
        if(mResultsFetcher == null || mResultsFetcher.isFinished() || mResultsFetcher.isCancelled()) {
            mResultsFetcher = mSearchManager.fetchResults(getApplicationContext(), searchId, callback);
        }
    }

    public void cancelResultsFetch() {
        if(mResultsFetcher != null) {
            mResultsFetcher.cancel();
            mResultsFetcher = null;
        }
    }

}
