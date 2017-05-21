package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.TradesmenAdapter;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.FILog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderDetailsFragment extends BaseFragment<OrderController>
        implements View.OnClickListener {

    private OrderDetailsInteractionListener mListener;
    private TradesmenAdapter mAdapter;

    private NestedScrollView mContentScroller;
    private EditText etReason;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        int previousAction = MotionEvent.INVALID_POINTER_ID;
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            v.postDelayed(new Runnable() {
                @Override
                public void run() {

                    int action = event.getActionMasked();

                    long eventDuration = event.getEventTime() - event.getDownTime();
                    FILog.i("View: " + v.getClass().getName() + ", event duration: " + eventDuration + ", action: " + action + ", previous action: " + previousAction);
                    if(previousAction == MotionEvent.ACTION_DOWN && action == MotionEvent.ACTION_UP) {
                        if (etReason.hasFocus()) {
                            View lastChild = mContentScroller.getChildAt(mContentScroller.getChildCount() - 1);
                            int bottom = lastChild.getBottom() + mContentScroller.getPaddingBottom();
                            int sy = mContentScroller.getScrollY();
                            int sh = mContentScroller.getHeight();
                            int delta = bottom - (sy + sh);

                            mContentScroller.smoothScrollBy(0, delta);
                        }
                    }

                    previousAction = action;
                }
            }, 350);
            return false;
        }
    };

    public static OrderDetailsFragment newInstance(ArrayList<TradesmanWrapper> selectedTradesmen) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.ARG_TRADESMEN, selectedTradesmen);
        args.putInt(Constants.ARG_TRADESMEN_COUNT, selectedTradesmen.size());
        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_details, container, false);

        setToolbar((Toolbar) v.findViewById(R.id.toolbar));

        int tradesmenCount = getArguments().getInt(Constants.ARG_TRADESMEN_COUNT);
        TextView tvSummary = (TextView) v.findViewById(R.id.tv_selection_description);
        tvSummary.setText(getString(R.string.selected_x_tradesmen, tradesmenCount));

        mAdapter = new TradesmenAdapter(getContext(), null);

        RecyclerView rvTradesmen = (RecyclerView) v.findViewById(R.id.rv_tradesmen);
        rvTradesmen.setAdapter(mAdapter);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(rvTradesmen);

        mContentScroller = (NestedScrollView) v.findViewById(R.id.scroll_content);
        mContentScroller.setOnTouchListener(mTouchListener);
        etReason = (EditText) v.findViewById(R.id.et_reason);
       // etReason.setOnTouchListener(mTouchListener);

        v.findViewById(R.id.btn_pick_a_reason).setOnClickListener(this);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        List<TradesmanWrapper> tradesmen = getArguments().getParcelableArrayList(Constants.ARG_TRADESMEN);
        mAdapter.setTradesmen(tradesmen);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OrderDetailsInteractionListener) {
            mListener = (OrderDetailsInteractionListener) context;
        } else {
            throw new IllegalStateException("context must implement " + OrderDetailsInteractionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_pick_a_reason) {
            if(mListener != null) {
                mListener.showOrderReasons();
            }
        }
    }

    public void setReason(String reason) {
        etReason.setText(reason);
    }

    public String getReason() {
        return etReason.getText().toString();
    }

    public interface OrderDetailsInteractionListener {
        void showOrderReasons();
    }

}
