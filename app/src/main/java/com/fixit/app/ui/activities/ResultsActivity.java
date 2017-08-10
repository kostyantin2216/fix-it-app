package com.fixit.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.TradesmanProfileFragment;
import com.fixit.app.ui.fragments.TradesmenResultsFragment;
import com.fixit.core.BaseApplication;
import com.fixit.core.controllers.ResultsController;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.Profession;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.general.SearchManager;
import com.fixit.core.ui.activities.BaseActivity;
import com.fixit.core.ui.fragments.ErrorFragment;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 3/30/2017.
 */

public class ResultsActivity extends BaseAppActivity<ResultsController>
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
                    .add(com.fixit.core.R.id.fragment_holder, new TradesmenResultsFragment(), FRAG_TAG_RESULTS_LIST)
                    .commit();
        }

        Intent intent = getIntent();
        String searchId = intent.getStringExtra(Constants.ARG_SEARCH_ID);
        getController().fetchResults(searchId, this);
        showLoader(getString(R.string.waiting_for_results));

        mJobLocation = intent.getParcelableExtra(Constants.ARG_JOB_LOCATION);
        mProfession = intent.getParcelableExtra(Constants.ARG_PROFESSION);
        mTradesmenIdsForPositions = new SparseArray<>();
    }

    @Override
    public ResultsController createController() {
        return new ResultsController((BaseApplication) getApplication(), this);
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
        getAnalyticsManager().trackTradesmanSelected(tradesman, mProfession.getName());
    }

    @Override
    public void orderTradesmen(List<TradesmanWrapper> selectedTradesmen) {
        getAnalyticsManager().trackResultsSelected(mProfession.getName(), selectedTradesmen.size());
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
