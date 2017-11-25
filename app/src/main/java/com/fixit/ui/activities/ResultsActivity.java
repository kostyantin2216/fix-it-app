package com.fixit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;

import com.fixit.app.R;
import com.fixit.FixxitApplication;
import com.fixit.controllers.ResultsController;
import com.fixit.data.JobLocation;
import com.fixit.data.Profession;
import com.fixit.data.Tradesman;
import com.fixit.data.TradesmanWrapper;
import com.fixit.general.SearchManager;
import com.fixit.ui.fragments.ErrorFragment;
import com.fixit.ui.helpers.UITutorials;
import com.fixit.utils.Constants;
import com.fixit.ui.fragments.TradesmanProfileFragment;
import com.fixit.ui.fragments.TradesmenResultsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 3/30/2017.
 */

public class ResultsActivity extends BaseActivity<ResultsController>
    implements SearchManager.ResultCallback,
               TradesmenResultsFragment.TradesmenResultsInteractionListener,
               TradesmanProfileFragment.TradesmanProfileInteractionListener {

    private final static String FRAG_TAG_RESULTS_LIST = "results_list";

    private JobLocation mJobLocation;
    private Profession mProfession;

    private SparseArray<String> mTradesmenIdsForPositions;

    private FloatingActionButton fabDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_appbar_fragment_holder);

        setToolbar((Toolbar) findViewById(R.id.toolbar), true);
        setToolbarTitleTextSize(22);
        setToolbarTitle(getString(R.string.no_tradesmen_selected));

        fabDone = (FloatingActionButton) findViewById(R.id.fab_done);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(com.fixit.app.R.id.fragment_holder, new TradesmenResultsFragment(), FRAG_TAG_RESULTS_LIST)
                    .commit();

            Intent intent = getIntent();
            String searchId = intent.getStringExtra(Constants.ARG_SEARCH_ID);
            getController().fetchResults(searchId, this);
            showLoader(getString(R.string.waiting_for_results));

            mJobLocation = intent.getParcelableExtra(Constants.ARG_JOB_LOCATION);
            mProfession = intent.getParcelableExtra(Constants.ARG_PROFESSION);
            mTradesmenIdsForPositions = new SparseArray<>();
        } else {
            finish();
        }
    }

    @Override
    public ResultsController createController() {
        return new ResultsController((FixxitApplication) getApplication(), this);
    }

    @Override
    public void onResultsReceived(List<Tradesman> tradesmen, Map<String, Integer> reviewCountForTradesmen) {
        hideLoader();
        getTradesmenResultsFragment().setTradesmen(tradesmen, reviewCountForTradesmen);
        getAnalyticsManager().trackSearchResults(mProfession.getName(), mJobLocation.getGoogleAddress(), tradesmen != null ? tradesmen.size() : 0);
    }

    @Override
    public void onResultsFetchTimeout() {
        showError(ErrorFragment.ErrorType.SERVER_UNAVAILABLE);
    }

    @Override
    public void showTradesman(int fromAdapterPosition, TradesmanWrapper tradesman) {
        mTradesmenIdsForPositions.put(fromAdapterPosition, tradesman.tradesman.get_id());
        TradesmanProfileFragment fragment = TradesmanProfileFragment.newInstance(tradesman.tradesman, true);
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
        getAnalyticsManager().trackTradesmanShown(tradesman.tradesman, mProfession.getName());
    }

    @Override
    public void onTradesmanSelected(Tradesman tradesman) {
        getSupportFragmentManager().popBackStack();
        int index = mTradesmenIdsForPositions.indexOfValue(tradesman.get_id());
        getTradesmenResultsFragment().selectTradesman(mTradesmenIdsForPositions.keyAt(index));
        getAnalyticsManager().trackTradesmanSelected(this, tradesman, mProfession.getName());
    }

    @Override
    public void orderTradesmen(List<TradesmanWrapper> selectedTradesmen) {
        getAnalyticsManager().trackResultsSelected(this, mProfession.getName(), selectedTradesmen.size());
        Intent intent = new Intent(this, OrderActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelableArrayList(Constants.ARG_TRADESMEN, (ArrayList<TradesmanWrapper>) selectedTradesmen);
        extras.putParcelable(Constants.ARG_JOB_LOCATION, mJobLocation);
        extras.putParcelable(Constants.ARG_PROFESSION, mProfession);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    @Override
    public void showDoneBtn() {
        fabDone.setVisibility(View.VISIBLE);
        UITutorials.create(UITutorials.TUTORIAL_ORDER_SELECTED_TRADESMEN, fabDone, getString(R.string.tutorial_order_selected_tradesmen))
            .show(getSupportFragmentManager());
    }

    @Override
    public void hideDoneBtn() {
        fabDone.setVisibility(View.GONE);
    }

    @Override
    public void setDoneBtnClickListener(View.OnClickListener onClickListener) {
        fabDone.setOnClickListener(onClickListener);
    }

    private TradesmenResultsFragment getTradesmenResultsFragment() {
        return (TradesmenResultsFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_RESULTS_LIST);
    }
}
