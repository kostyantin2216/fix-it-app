package com.fixit.feedback;

import com.fixit.data.Order;

/**
 * Created by Kostyantin on 8/17/2017.
 */

public class OrderDelayedFeedbackFlow extends BaseOrderFeedbackFlow {

    public OrderDelayedFeedbackFlow(Order mOrder, OrderFeedbackView mView, boolean fromAction, boolean yesAction) {
        super(mOrder, mView);
    }

    @Override
    public void onSelectionMade(int selectionCode, Object selection) {
        switch (selectionCode) {

            default:
                super.onSelectionMade(selectionCode, selection);
        }
    }

    @Override
    public void onTradesmanReviewed() {

    }
}
