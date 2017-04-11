package com.fixit.app.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fixit.app.ui.fragments.TradesmenResultsFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.ResultsController;
import com.fixit.core.controllers.SearchController;
import com.fixit.core.data.Tradesman;
import com.fixit.core.general.SearchManager;
import com.fixit.core.ui.activities.BaseActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 3/30/2017.
 */

public class ResultsActivity extends BaseActivity<ResultsController>
    implements SearchManager.ResultCallback,
               TradesmenResultsFragment.TradesmenResultsInteractionListener {

    private final static String FRAG_TAG_RESULTS_LIST = "results_list";

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
        getTradesmenResultsFragment().setTradesmen(tradesmen, reviewCountForTradesmen);
    }

    @Override
    public void onResultsFetchTimeout() {

    }

    private TradesmenResultsFragment getTradesmenResultsFragment() {
        return (TradesmenResultsFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_RESULTS_LIST);
    }
}
