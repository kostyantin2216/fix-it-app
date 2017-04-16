package com.fixit.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.TradesmenResultsFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.ResultsController;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.Tradesman;
import com.fixit.core.general.SearchManager;
import com.fixit.core.ui.activities.BaseActivity;
import com.fixit.core.utils.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 3/30/2017.
 */

public class ResultsActivity extends BaseActivity<ResultsController>
    implements SearchManager.ResultCallback,
               TradesmenResultsFragment.TradesmenResultsInteractionListener {

    private final static String FRAG_TAG_RESULTS_LIST = "results_list";

    private JobLocation mJobLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fixit.core.R.layout.layout_fragment_holder);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(com.fixit.core.R.id.fragment_holder, new TradesmenResultsFragment(), FRAG_TAG_RESULTS_LIST)
                    .commit();
        }

        Intent intent = getIntent();
        String searchId = intent.getStringExtra(Constants.ARG_SEARCH_ID);
        getController().fetchResults(searchId, this);
        showLoader(getString(R.string.waiting_for_results));

        mJobLocation = intent.getParcelableExtra(Constants.ARG_JOB_LOCATION);
    }

    @Override
    public ResultsController createController() {
        return new ResultsController((BaseApplication) getApplication());
    }


    @Override
    public void onTradesmanSelected(Tradesman tradesman) {

    }

    @Override
    public void onResultsReceived(List<Tradesman> tradesmen, Map<String, Integer> reviewCountForTradesmen) {
        hideLoader();
        getTradesmenResultsFragment().setTradesmen(tradesmen, reviewCountForTradesmen);
    }

    @Override
    public void onResultsFetchTimeout() {

    }

    private TradesmenResultsFragment getTradesmenResultsFragment() {
        return (TradesmenResultsFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_RESULTS_LIST);
    }
}
