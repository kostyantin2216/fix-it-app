package com.fixit.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.controllers.OrderController;
import com.fixit.data.Profession;
import com.fixit.data.TradesmanWrapper;
import com.fixit.ui.components.ExpandablePanel;
import com.fixit.ui.helpers.TradesmanActionHandler;
import com.fixit.ui.helpers.UITutorials;
import com.fixit.utils.Constants;
import com.fixit.ui.adapters.TradesmenAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderDetailsFragment extends BaseFragment<OrderController>
        implements View.OnClickListener,
                   ExpandablePanel.ExpandablePanelListener,
                   TradesmenAdapter.TradesmenAdapterCallback {

    private OrderDetailsInteractionListener mListener;
    private TradesmenAdapter mAdapter;

    private ViewHolder mView;

    public static OrderDetailsFragment newInstance(Profession profession, String address, ArrayList<TradesmanWrapper> selectedTradesmen) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_PROFESSION, profession);
        args.putString(Constants.ARG_ADDRESS, address);
        args.putParcelableArrayList(Constants.ARG_TRADESMEN, selectedTradesmen);
        args.putInt(Constants.ARG_TRADESMEN_COUNT, selectedTradesmen.size());
        fragment.setArguments(args);

        return fragment;
    }

    private enum ViewState {
        BEFORE_ORDER,
        AFTER_ORDER
    }

    static class ViewHolder implements View.OnFocusChangeListener {

        final TextView tvProfession;
        final TextView tvLocation;
        final ExpandablePanel tradesmenPanel;
        final RecyclerView rvTradesmen;
        final EditText etComment;
        final Button btnPickReason;
        final FloatingActionButton fabDone;

        private ViewState mState = ViewState.BEFORE_ORDER;

        ViewHolder(View v, Profession profession, String address, TradesmenAdapter recyclerAdapter, int tradesmenCount, ExpandablePanel.ExpandablePanelListener panelListener, View.OnClickListener onClickListener) {
            tvProfession = (TextView) v.findViewById(R.id.tv_profession);
            tvProfession.setText(profession.getName());

            tvLocation = (TextView) v.findViewById(R.id.tv_location);
            tvLocation.setText(address);

            tradesmenPanel = (ExpandablePanel) v.findViewById(R.id.panel_tradesmen);
            tradesmenPanel.setTitle(v.getResources().getString(R.string.chosen_x_tradesmen, tradesmenCount));
            tradesmenPanel.setListener(panelListener);

            rvTradesmen = (RecyclerView) v.findViewById(R.id.rv_tradesmen);
            rvTradesmen.setAdapter(recyclerAdapter);
            SnapHelper helper = new LinearSnapHelper();
            helper.attachToRecyclerView(rvTradesmen);

            etComment = (EditText) v.findViewById(R.id.et_comment);
            etComment.setOnFocusChangeListener(this);

            btnPickReason = (Button) v.findViewById(R.id.btn_pick_a_reason);
            btnPickReason.setOnClickListener(onClickListener);

            fabDone = (FloatingActionButton) v.findViewById(R.id.fab_done);
            fabDone.setOnClickListener(onClickListener);
        }

        void setState(ViewState state) {
            mState = state;
            switch (state) {
                case BEFORE_ORDER:
                    etComment.setEnabled(true);
                    btnPickReason.setVisibility(View.VISIBLE);
                    fabDone.setImageDrawable(ContextCompat.getDrawable(fabDone.getContext(), R.drawable.ic_check_white_24dp));
                    break;
                case AFTER_ORDER:
                    etComment.setEnabled(false);
                    btnPickReason.setVisibility(View.GONE);
                    fabDone.setImageDrawable(ContextCompat.getDrawable(fabDone.getContext(), R.drawable.ic_close_white_24dp));
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
            etComment.clearFocus();
        }

        String getComment() {
            return etComment.getText().toString();
        }

        void setComment(String reason) {
            etComment.setText(reason);
        }

        void setJobReasonCount(int count) {
            String text;
            Resources res = btnPickReason.getResources();
            if(count == 0) {
                text = res.getString(R.string.pick_a_reason);
            } else {
                text = res.getQuantityString(R.plurals.job_reasons_picked, count, count);
            }
            btnPickReason.setText(text);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_details, container, false);

        setToolbar((Toolbar) v.findViewById(R.id.toolbar), true);

        Bundle args = getArguments();
        Profession profession = args.getParcelable(Constants.ARG_PROFESSION);
        String address = args.getString(Constants.ARG_ADDRESS);
        int tradesmenCount = args.getInt(Constants.ARG_TRADESMEN_COUNT);

        mAdapter = new TradesmenAdapter(this, 0.5f, true);

        mView = new ViewHolder(v, profession, address, mAdapter, tradesmenCount, this, this);

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

        mView.rvTradesmen.post(() -> {
            UITutorials.create(UITutorials.TUTORIAL_ORDER_DETAILS_SCREEN, mView.tradesmenPanel, getString(R.string.tutorial_slide_for_tradesmen))
                    .and(mView.rvTradesmen.findViewHolderForLayoutPosition(0).itemView, getString(R.string.tutorial_view_profile))
                    .and(mView.etComment, getString(R.string.tutorial_order_comment))
                    .and(mView.btnPickReason, getString(R.string.tutorial_select_reasons))
                    .and(mView.fabDone, getString(R.string.tutorial_complete_order))
                    .show(getFragmentManager());
        });
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
                    switch (mView.mState) {
                        case BEFORE_ORDER:
                            mListener.completeOrder(mView.getComment());
                            mView.setState(ViewState.AFTER_ORDER);
                            mView.showTradesmen();
                            break;
                        case AFTER_ORDER:
                            restartApp(true);
                            break;
                    }
                    break;
            }
            mView.clearFocus();
        }
    }

    @Override
    public void onTradesmanClick(int adapterPosition, TradesmanWrapper tradesman) {
        TradesmanActionHandler.showTradesman(getFragmentManager(), tradesman.tradesman);
    }

    @Override
    public void onTradesmanUnselected(boolean hasMoreSelections) {

    }

    @Override
    public void onPanelExpanded(ExpandablePanel panel) {
        hideKeyboard(getView());
        mView.clearFocus();
    }

    @Override
    public void onPanelCollapsed(ExpandablePanel panel) { }

    public void onJobReasonSelectionChanged(int jobReasonCount) {
        mView.setJobReasonCount(jobReasonCount);
    }

    public String getComment() {
        return mView.getComment();
    }

    public interface OrderDetailsInteractionListener {
        void showOrderReasons();
        void completeOrder(String reason);
    }

}
