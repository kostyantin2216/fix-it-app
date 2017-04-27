package com.fixit.core.controllers;

import com.fixit.core.BaseApplication;
import com.fixit.core.general.SearchManager;

/**
 * Created by konstantin on 4/3/2017.
 */

public class ResultsController extends TradesmenController {

    private final SearchManager mSearchManager;

    private SearchManager.ResultsFetcher mResultsFetcher;

    public ResultsController(BaseApplication baseApplication) {
        super(baseApplication);
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
