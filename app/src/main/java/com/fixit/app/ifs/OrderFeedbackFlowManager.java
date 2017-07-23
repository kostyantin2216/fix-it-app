package com.fixit.app.ifs;

import android.support.v4.app.Fragment;

import com.fixit.app.R;
import com.fixit.app.ui.fragments.SingleChoiceSelectionFragment;
import com.fixit.app.ui.fragments.TradesmanReviewFragment;
import com.fixit.core.data.Order;
import com.fixit.core.data.Tradesman;
import com.fixit.core.utils.Constants;

/**
 * Created by konstantin on 7/23/2017.
 */

public class OrderFeedbackFlowManager implements SingleChoiceSelectionFragment.SingleChoiceSelectionListener {

    private final static int SELECTION_CODE_CONTACTED = 1;
    private final static int SELECTION_CODE_ARRIVED = 2;
    private final static int SELECTION_CODE_NEW_SEARCH = 3;
    private final static int SELECTION_CODE_TRADESMAN = 4;
    private final static int SELECTION_CODE_SEARCH_PARAMS = 5;
    private final static int SELECTION_CODE_JOB_RATING = 6;
    private final static int SELECTION_CODE_GOOGLE_RATING = 7;

    private final Order mOrder;
    private final OrderFeedbackView mView;

    public OrderFeedbackFlowManager(Order order, OrderFeedbackView feedbackView, boolean fromAction, boolean yesAction) {
        this.mOrder = order;
        this.mView = feedbackView;

        if (fromAction) {
            hasBeenContacted(yesAction);
        } else {
            mView.transitionTo(SingleChoiceSelectionFragment.newInstance(mView.getString(R.string.order_notification_title), SELECTION_CODE_CONTACTED));
        }
    }

    @Override
    public SingleChoiceSelectionFragment.SelectionBuilder getSelections(int selectionCode) {
        switch (selectionCode) {
            case SELECTION_CODE_CONTACTED:
            case SELECTION_CODE_ARRIVED:
            case SELECTION_CODE_SEARCH_PARAMS:
            case SELECTION_CODE_JOB_RATING:
                return new SingleChoiceSelectionFragment.SelectionBuilder()
                        .add(mView.getString(com.fixit.core.R.string.yes), true)
                        .add(mView.getString(com.fixit.core.R.string.no), false);
            case SELECTION_CODE_NEW_SEARCH:
                return new SingleChoiceSelectionFragment.SelectionBuilder()
                        .add(mView.getString(com.fixit.core.R.string.no), 0)
                        .add(mView.getString(com.fixit.core.R.string.yes), 1)
                        .add(mView.getString(R.string.later), 2);
            case SELECTION_CODE_TRADESMAN:
                SingleChoiceSelectionFragment.SelectionBuilder selectionBuilder = new SingleChoiceSelectionFragment.SelectionBuilder();
                for(Tradesman tradesman : mOrder.getTradesmen()) {
                    selectionBuilder.add(tradesman.getCompanyName(), tradesman);
                }
                return selectionBuilder;
            case SELECTION_CODE_GOOGLE_RATING:
                return new SingleChoiceSelectionFragment.SelectionBuilder()
                        .add(mView.getString(R.string.okay), true)
                        .add(mView.getString(R.string.not_now), false);
        }

        return null;
    }

    @Override
    public void onSelectionMade(int selectionCode, Object selection) {
        switch (selectionCode) {
            case SELECTION_CODE_CONTACTED:
                hasBeenContacted((boolean) selection);
                break;
            case SELECTION_CODE_ARRIVED:
                tradesmanArrived((boolean) selection);
                break;
            case SELECTION_CODE_NEW_SEARCH:
                newSearch((int) selection);
                break;
            case SELECTION_CODE_TRADESMAN:
                tradesmanSelected((Tradesman) selection);
                break;
            case SELECTION_CODE_SEARCH_PARAMS:
                startSearch((Boolean) selection);
                break;
            case SELECTION_CODE_JOB_RATING:
                jobRated((Boolean) selection);
                break;
            case SELECTION_CODE_GOOGLE_RATING:
                rateOnGoogle((Boolean) selection);
                break;
        }
    }

    private void hasBeenContacted(boolean contacted) {
        if(contacted) {
            transitionToSingleChoiceSelection(mView.getString(R.string.arrived_to_fix_problem), SELECTION_CODE_ARRIVED);
        } else {
            transitionToSingleChoiceSelection(mView.getString(R.string.like_to_start_a_new_search), SELECTION_CODE_NEW_SEARCH);
        }
    }

    private void tradesmanArrived(boolean arrived) {
        if(arrived) {
            transitionToSingleChoiceSelection(mView.getString(R.string.who_fixed_the_problem), SELECTION_CODE_TRADESMAN);
        } else {
            transitionToSingleChoiceSelection(mView.getString(R.string.like_to_start_a_new_search), SELECTION_CODE_NEW_SEARCH);
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

    private void startSearch(boolean usePreviousSearchParams) {

    }

    private void jobRated(boolean goodRating) {
        if(goodRating) {
            transitionToSingleChoiceSelection(mView.getString(R.string.please_rate_us_on_play_store), SELECTION_CODE_GOOGLE_RATING);
        }
    }

    private void tradesmanSelected(Tradesman tradesman) {
        mView.transitionTo(TradesmanReviewFragment.newInstance(tradesman));
    }

    public void tradesmanReviewed() {
        transitionToSingleChoiceSelection(mView.getString(R.string.has_the_job_been_complete_to_your_liking), SELECTION_CODE_JOB_RATING);
    }

    private void transitionToSingleChoiceSelection(String title, int selectionCode) {
        mView.transitionTo(SingleChoiceSelectionFragment.newInstance(title, selectionCode));
    }

    private void rateOnGoogle(boolean userAgreed) {
        if(userAgreed) {

        }
        mView.closeApp();
    }

    public interface OrderFeedbackView {
        void transitionTo(Fragment fragment);
        String getString(int resId);
        String getString(int resId, Object... formatArgs);
        void closeApp();
    }

}
