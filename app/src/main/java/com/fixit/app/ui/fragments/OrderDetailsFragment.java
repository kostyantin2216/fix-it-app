package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.TradesmenAdapter;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.ui.components.ExpandablePanel;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderDetailsFragment extends BaseFragment<OrderController>
        implements View.OnClickListener,
                   ExpandablePanel.ExpandablePanelListener {

    private OrderDetailsInteractionListener mListener;
    private TradesmenAdapter mAdapter;

    private ViewHolder mView;

    public static OrderDetailsFragment newInstance(ArrayList<TradesmanWrapper> selectedTradesmen) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.ARG_TRADESMEN, selectedTradesmen);
        args.putInt(Constants.ARG_TRADESMEN_COUNT, selectedTradesmen.size());
        fragment.setArguments(args);

        return fragment;
    }

    enum ViewState {
        BEFORE_ORDER,
        AFTER_ORDER
    }

    static class ViewHolder implements View.OnFocusChangeListener {

        final ExpandablePanel tradesmenPanel;
        final EditText etReason;
        final Button btnPickReason;

        public ViewHolder(View v, TradesmenAdapter recyclerAdapter, int tradesmenCount, ExpandablePanel.ExpandablePanelListener panelListener, View.OnClickListener onClickListener) {
            tradesmenPanel = (ExpandablePanel) v.findViewById(R.id.panel_tradesmen);
            tradesmenPanel.setTitle(v.getResources().getString(R.string.selected_x_tradesmen, tradesmenCount));
            tradesmenPanel.setListener(panelListener);

            RecyclerView rvTradesmen = (RecyclerView) v.findViewById(R.id.rv_tradesmen);
            rvTradesmen.setAdapter(recyclerAdapter);
            SnapHelper helper = new LinearSnapHelper();
            helper.attachToRecyclerView(rvTradesmen);

            etReason = (EditText) v.findViewById(R.id.et_reason);
            etReason.setOnFocusChangeListener(this);

            btnPickReason = (Button) v.findViewById(R.id.btn_pick_a_reason);
            btnPickReason.setOnClickListener(onClickListener);

            v.findViewById(R.id.fab_done).setOnClickListener(onClickListener);
        }

        public void setState(ViewState state) {
            switch (state) {
                case BEFORE_ORDER:
                    etReason.setEnabled(true);
                    btnPickReason.setVisibility(View.VISIBLE);
                    break;
                case AFTER_ORDER:
                    etReason.setEnabled(false);
                    btnPickReason.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                tradesmenPanel.collapse();
            } else {
                tradesmenPanel.expand();
            }
        }

        void showTradesmen() {
            tradesmenPanel.expand();
        }

        void hideTradesmen() {
            tradesmenPanel.collapse();
        }

        void clearFocus() {
            etReason.clearFocus();
        }

        String getReason() {
            return etReason.getText().toString();
        }

        void setReason(String reason) {
            etReason.setText(reason);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_details, container, false);

        setToolbar((Toolbar) v.findViewById(R.id.toolbar));

        int tradesmenCount = getArguments().getInt(Constants.ARG_TRADESMEN_COUNT);

        mAdapter = new TradesmenAdapter(null, 0.5f);

        mView = new ViewHolder(v, mAdapter, tradesmenCount, this, this);

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
        if(mListener != null) {
            switch (v.getId()) {
                case R.id.btn_pick_a_reason:
                    mListener.showOrderReasons();
                    break;
                case R.id.fab_done:
                    mListener.completeOrder(mView.getReason());
                    mView.setState(ViewState.AFTER_ORDER);
                    mView.showTradesmen();
                    break;
            }
        }
    }

    @Override
    public void onPanelExpanded(ExpandablePanel panel) {
        hideKeyboard(getView());
        mView.clearFocus();
    }

    @Override
    public void onPanelCollapsed(ExpandablePanel panel) { }

    public void setReason(String reason) {
        mView.setReason(reason);
    }

    public interface OrderDetailsInteractionListener {
        void showOrderReasons();
        void completeOrder(String reason);
    }

}
