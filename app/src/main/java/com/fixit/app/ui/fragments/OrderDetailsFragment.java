package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.TradesmenAdapter;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.utils.Constants;

import java.util.ArrayList;

/**
 * Created by konstantin on 5/16/2017.
 */

public class OrderDetailsFragment extends BaseFragment<OrderController>
        implements View.OnClickListener {

    private OrderDetailsInteractionListener mListener;
    private TradesmenAdapter mAdapter;

    private EditText etReason;

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

        int tradesmenCount = getArguments().getInt(Constants.ARG_TRADESMEN_COUNT);
        TextView tvSelectionDesc = (TextView) v.findViewById(R.id.tv_selection_description);
        tvSelectionDesc.setText(getString(R.string.selected_x_tradesmen, tradesmenCount));

        mAdapter = new TradesmenAdapter(getContext(), null);

        RecyclerView rvTradesmen = (RecyclerView) v.findViewById(R.id.rv_tradesmen);
        rvTradesmen.setAdapter(mAdapter);

        etReason = (EditText) v.findViewById(R.id.et_reason);

        v.findViewById(R.id.btn_pick_a_reason).setOnClickListener(this);

        return v;
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
