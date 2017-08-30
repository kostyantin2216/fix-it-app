package com.fixit.feedback;

import com.fixit.app.R;
import com.fixit.data.Order;
import com.fixit.data.Tradesman;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kostyantin on 8/17/2017.
 */

public class OrderImmediateFeedbackFlow extends BaseOrderFeedbackFlow {

    public OrderImmediateFeedbackFlow(Order order, OrderFeedbackView view, boolean fromAction, boolean yesAction) {
        super(order, view);

        if (fromAction) {
            hasBeenContacted(yesAction);
        } else {
            transitionToSingleChoiceSelection(view.getString(R.string.order_notification_immediate_flow_message), SELECTION_CODE_CONTACTED);
        }
    }

    @Override
    public void onSelectionMade(int selectionCode, Object selection) {
        switch (selectionCode) {
            case SELECTION_CODE_CONTACTED:
                hasBeenContacted((boolean) selection);
                break;
            case SELECTION_CODE_TRADESMEN:
                onTradesmenSelected((List<Tradesman>) selection);
                break;
            case SELECTION_CODE_SATISFACTION:
                answeredToSatisfaction((boolean) selection);
                break;
            case SELECTION_CODE_SATISFACTION_REASON:
                reasonOfSatisfaction((String) selection);
            default:
                super.onSelectionMade(selectionCode, selection);
        }
    }

    private void hasBeenContacted(boolean yesAction) {
        if(yesAction) {
            Tradesman[] tradesmen = getOrder().getTradesmen();
            if(tradesmen.length == 1) {
                onTradesmenSelected(Arrays.asList(tradesmen));
            } else {
                transitionToMultiChoiceSelection(getView().getString(R.string.pick_tradesmen_who_contacted_you), SELECTION_CODE_TRADESMEN);
            }
        } else {
            transitionToNewSearchFlow();
        }
    }

    private void onTradesmenSelected(List<Tradesman> tradesmen) {
        String question = getView().getString(
                tradesmen.size() == 1
                        ? R.string.singular_answered_to_your_satisfaction
                        : R.string.plural_answered_to_your_satisfaction
        );
        transitionToSingleChoiceSelection(question, SELECTION_CODE_SATISFACTION);
    }

    private void answeredToSatisfaction(boolean yes) {
        if(yes) {
            transitionToFinalizeFlow();
        } else {
            transitionToInputChoiceSelection(getView().getString(R.string.why_not_answered_to_satisfaction), SELECTION_CODE_SATISFACTION_REASON);
        }
    }

    private void reasonOfSatisfaction(String reason) {
        transitionToFinalizeFlow();
    }

    @Override
    public void onTradesmanReviewed() {

    }
}
