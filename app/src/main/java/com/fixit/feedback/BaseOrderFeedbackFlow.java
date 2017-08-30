package com.fixit.feedback;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.fixit.app.R;
import com.fixit.data.Order;
import com.fixit.data.Tradesman;
import com.fixit.utils.Constants;
import com.fixit.ui.activities.SearchActivity;
import com.fixit.ui.fragments.feedback.ChoiceSelectionFragment;

/**
 * Created by Kostyantin on 8/17/2017.
 */

public abstract class BaseOrderFeedbackFlow {

    protected final static int SELECTION_CODE_CONTACTED = 1;
    protected final static int SELECTION_CODE_ARRIVED = 2;
    protected final static int SELECTION_CODE_NEW_SEARCH = 3;
    protected final static int SELECTION_CODE_TRADESMAN = 4;
    protected final static int SELECTION_CODE_SEARCH_PARAMS = 5;
    protected final static int SELECTION_CODE_JOB_RATING = 6;
    protected final static int SELECTION_CODE_GOOGLE_RATING = 7;
    protected final static int SELECTION_CODE_TRADESMEN = 8;
    protected final static int SELECTION_CODE_SATISFACTION = 9;
    protected final static int SELECTION_CODE_FINALIZE_FLOW = 10;
    protected final static int SELECTION_CODE_SATISFACTION_REASON = 11;

    private final Order mOrder;
    private final OrderFeedbackView mView;

    public BaseOrderFeedbackFlow(Order mOrder, OrderFeedbackView mView) {
        this.mOrder = mOrder;
        this.mView = mView;
    }

    protected Order getOrder() {
        return mOrder;
    }

    protected OrderFeedbackView getView() {
        return mView;
    }

    public ChoiceSelection.Builder getSelections(int selectionCode) {
        switch (selectionCode) {
            case SELECTION_CODE_CONTACTED:
            case SELECTION_CODE_ARRIVED:
            case SELECTION_CODE_SEARCH_PARAMS:
            case SELECTION_CODE_JOB_RATING:
            case SELECTION_CODE_SATISFACTION:
                return new ChoiceSelection.Builder()
                        .add(mView.getString(com.fixit.app.R.string.yes), true)
                        .add(mView.getString(com.fixit.app.R.string.no), false);
            case SELECTION_CODE_NEW_SEARCH:
                return new ChoiceSelection.Builder()
                        .add(mView.getString(com.fixit.app.R.string.no), 0)
                        .add(mView.getString(com.fixit.app.R.string.yes), 1);
            //            .add(mView.getString(R.string.later), 2);
            case SELECTION_CODE_TRADESMAN:
            case SELECTION_CODE_TRADESMEN:
                ChoiceSelection.Builder selectionBuilder = new ChoiceSelection.Builder();
                for(Tradesman tradesman : mOrder.getTradesmen()) {
                    selectionBuilder.add(tradesman.getCompanyName(), tradesman);
                }
                return selectionBuilder;
            case SELECTION_CODE_GOOGLE_RATING:
                return new ChoiceSelection.Builder()
                        .add(mView.getString(R.string.okay), true)
                        .add(mView.getString(R.string.not_now), false);
            case SELECTION_CODE_FINALIZE_FLOW:
                return new ChoiceSelection.Builder()
                        .add(mView.getString(R.string.view_order_history), 0)
                        .add(mView.getString(R.string.new_search), 1)
                        .add(mView.getString(R.string.leave_app), 2);
        }

        return null;
    }

    public void onSelectionMade(int selectionCode, Object selection) {
        switch (selectionCode) {
            case SELECTION_CODE_NEW_SEARCH:
                newSearch((int) selection);
                break;
            case SELECTION_CODE_SEARCH_PARAMS:
                startSearch((boolean) selection);
                break;
            case SELECTION_CODE_FINALIZE_FLOW:
                finalizeFlow((int) selection);
                break;
        }
    }

    public abstract void onTradesmanReviewed();

    private void finalizeFlow(int decision) {
        switch (decision) {
            case 0:
                openOrderHistory();
                break;
            case 1:
                newSearch(1);
                break;
            case 2:
                mView.closeApp();
                break;
        }
    }

    private void newSearch(int decision) {
        switch (decision) {
            case 0:
                mView.closeApp();
                break;
            case 1:
                transitionToSingleChoiceSelection(
                        mView.getString(
                                R.string.use_same_profession_and_location,
                                mOrder.getProfession().getName(),
                                mOrder.getJobLocation().getGoogleAddress()
                        ),
                        SELECTION_CODE_SEARCH_PARAMS
                );
                break;
            case 2:
                break;
        }
    }

    private void openOrderHistory() {
        Intent intent = mView.createIntent(SearchActivity.class);
        intent.putExtra(Constants.ARG_DELEGATE, SearchActivity.DELEGATE_ORDER_HISTORY);
        mView.startActivity(intent);
        mView.finish();
    }

    private void startSearch(boolean usePreviousSearchParams) {
        Intent intent = mView.createIntent(SearchActivity.class);
        if(usePreviousSearchParams) {
            intent.putExtra(Constants.ARG_ORDER_TO_RESTORE, mOrder.getId());
        }
        mView.startActivity(intent);
        mView.finish();
    }

    public void transitionToSingleChoiceSelection(String title, int selectionCode) {
        mView.transitionTo(ChoiceSelectionFragment.newInstance(
                ChoiceSelectionFragment.SINGLE_CHOICE, title, selectionCode
        ));
        onTransition(title, selectionCode);
    }

    public void transitionToMultiChoiceSelection(String title, int selectionCode) {
        mView.transitionTo(ChoiceSelectionFragment.newInstance(
                ChoiceSelectionFragment.MULTI_CHOICE, title, selectionCode
        ));
        onTransition(title, selectionCode);
    }

    public void transitionToInputChoiceSelection(String title, int selectionCode) {
        mView.transitionTo(ChoiceSelectionFragment.newInstance(
                ChoiceSelectionFragment.INPUT_CHOICE, title, selectionCode
        ));
        onTransition(title, selectionCode);
    }

    public void transitionToNewSearchFlow() {
        String title = mView.getString(R.string.like_to_start_a_new_search);
        mView.transitionTo(ChoiceSelectionFragment.newInstance(
                ChoiceSelectionFragment.SINGLE_CHOICE,
                title,
                SELECTION_CODE_NEW_SEARCH
        ));
        onTransition(title, SELECTION_CODE_NEW_SEARCH);
    }

    protected void transitionToFinalizeFlow() {
        String title = mView.getString(R.string.what_would_you_like_to_do_next);
        mView.transitionTo(ChoiceSelectionFragment.newInstance(
                ChoiceSelectionFragment.SINGLE_CHOICE,
                title,
                SELECTION_CODE_FINALIZE_FLOW
        ));
        onTransition(title, SELECTION_CODE_FINALIZE_FLOW);
    }

    public void transitionToFragment(Fragment fragment) {
        mView.transitionTo(fragment);
    }

    private void onTransition(String title, int selectionCode) {

    }

}
