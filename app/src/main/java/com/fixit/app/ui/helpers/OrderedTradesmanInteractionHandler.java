package com.fixit.app.ui.helpers;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.fixit.app.R;
import com.fixit.core.data.Tradesman;

/**
 * Created by konstantin on 8/8/2017.
 */

public class OrderedTradesmanInteractionHandler {

    private OrderedTradesmanInteractionListener mListener;

    public OrderedTradesmanInteractionHandler() { }

    public OrderedTradesmanInteractionHandler(OrderedTradesmanInteractionListener listener) {
        mListener = listener;
    }

    public void setListener(OrderedTradesmanInteractionListener listener) {
        mListener = listener;
    }

    public void onClick(View v, final Tradesman tradesman) {
        if(mListener != null) {
            switch (v.getId()) {
                case R.id.iv_more:
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(mListener != null) {
                                switch (item.getItemId()) {
                                    case R.id.view:
                                        viewTradesman(tradesman);
                                        return true;
                                    case R.id.review:
                                        reviewTradesman(tradesman);
                                        return true;
                                }
                            }
                            return false;
                        }
                    });
                    popup.inflate(R.menu.tradesman);
                    popup.show();
                    break;
                case R.id.iv_call:
                    callTradesman(tradesman);
                    break;
                case R.id.iv_msg:
                    messageTradesman(tradesman);
                    break;
            }
        }
    }

    private void viewTradesman(Tradesman tradesman) {
        mListener.onOrderViewInteraction();
        mListener.showTradesman(tradesman);
    }

    private void reviewTradesman(Tradesman tradesman) {
        mListener.onOrderViewInteraction();
        mListener.reviewTradesman(tradesman);
    }

    private void callTradesman(Tradesman tradesman) {
        mListener.onOrderViewInteraction();
    }

    private void messageTradesman(Tradesman tradesman) {
        mListener.onOrderViewInteraction();
    }

    public interface OrderedTradesmanInteractionListener {
        void onOrderViewInteraction();
        void showTradesman(Tradesman tradesman);
        void reviewTradesman(Tradesman tradesman);
    }

}
