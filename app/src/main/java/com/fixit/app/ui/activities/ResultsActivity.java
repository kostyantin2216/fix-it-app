package com.fixit.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.TradesmanProfileFragment;
import com.fixit.app.ui.fragments.TradesmenResultsFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.ResultsController;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.general.SearchManager;
import com.fixit.core.ui.activities.BaseActivity;
import com.fixit.core.ui.fragments.ErrorFragment;
import com.fixit.core.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 3/30/2017.
 */

public class ResultsActivity extends BaseAppActivity<ResultsController>
    implements SearchManager.ResultCallback,
               TradesmenResultsFragment.TradesmenResultsInteractionListener,
               TradesmanProfileFragment.TradesmanProfileInteractionListener,
               BaseActivity.LoginRequester {

    private final static String FRAG_TAG_RESULTS_LIST = "results_list";

    private JobLocation mJobLocation;

    private SparseArray<String> mTradesmenIdsForPositions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_appbar_fragment_holder);

        setToolbar((Toolbar) findViewById(R.id.toolbar), false);

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
        mTradesmenIdsForPositions = new SparseArray<>();
    }

    @Override
    public ResultsController createController() {
        return new ResultsController((BaseApplication) getApplication());
    }

    @Override
    public void onResultsReceived(List<Tradesman> tradesmen, Map<String, Integer> reviewCountForTradesmen) {
        hideLoader();
        getTradesmenResultsFragment().setTradesmen(tradesmen, reviewCountForTradesmen);
    }

    @Override
    public void onResultsFetchTimeout() {
        showError(ErrorFragment.ErrorType.SERVER_UNAVAILABLE);
    }

    @Override
    public void showTradesman(int fromAdapterPosition, TradesmanWrapper tradesman) {
        mTradesmenIdsForPositions.put(fromAdapterPosition, tradesman.tradesman.get_id());
        TradesmanProfileFragment fragment = TradesmanProfileFragment.newInstance(tradesman.tradesman);
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onTradesmanSelected(Tradesman tradesman) {
        getSupportFragmentManager().popBackStack();
        int index = mTradesmenIdsForPositions.indexOfValue(tradesman.get_id());
        getTradesmenResultsFragment().selectTradesman(mTradesmenIdsForPositions.keyAt(index));
    }

    @Override
    public void orderTradesmen(List<TradesmanWrapper> selectedTradesmen) {
        if(isUserRegistered()) {
            continueToOrder((ArrayList<TradesmanWrapper>) selectedTradesmen);
        } else {
            requestLogin(this, createOrderExtras((ArrayList<TradesmanWrapper>) selectedTradesmen));
        }
    }

    @Override
    public void loginComplete(boolean success, Bundle data) {
        if(success) {
            if (data.containsKey(Constants.ARG_TRADESMAN)) {
                ArrayList<TradesmanWrapper> tradesmen = data.getParcelableArrayList(Constants.ARG_TRADESMEN);
                continueToOrder(tradesmen);
            } else {
                notifyUser("no tradesmen");
            }
        } else {
            showPrompt(getString(R.string.cannot_continue_without_login));
        }
    }

    private void continueToOrder(ArrayList<TradesmanWrapper> tradesmen) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtras(createOrderExtras(tradesmen));
        startActivity(intent);
    }

    private Bundle createOrderExtras(ArrayList<TradesmanWrapper> tradesmen) {
        Bundle extras = new Bundle();
        extras.putParcelableArrayList(Constants.ARG_TRADESMEN, tradesmen);
        extras.putParcelable(Constants.ARG_JOB_LOCATION, mJobLocation);
        return extras;
    }

    private TradesmenResultsFragment getTradesmenResultsFragment() {
        return (TradesmenResultsFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_RESULTS_LIST);
    }
}
