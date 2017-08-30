package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fixit.app.R;
import com.fixit.config.AppConfig;
import com.fixit.controllers.ResultsController;
import com.fixit.data.Tradesman;
import com.fixit.data.TradesmanWrapper;
import com.fixit.ui.adapters.TradesmenAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 4/3/2017.
 */
public class TradesmenResultsFragment extends StaticRecyclerListFragment<ResultsController>
    implements TradesmenAdapter.TradesmenAdapterCallback,
               View.OnClickListener {

    private TradesmenResultsInteractionListener mListener;
    private TradesmenAdapter mAdapter;

    private int mMaxTradesmanSelection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMaxTradesmanSelection = AppConfig.getInteger(getContext(), AppConfig.KEY_MAX_TRADESMEN_SELECTION, 3);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setBackground(R.drawable.bg_brick_wall);

        mAdapter = new TradesmenAdapter(mMaxTradesmanSelection, this);
        setAdapter(mAdapter);
    }

    @Override
    public String getEmptyListMessage() {
        return getString(R.string.no_tradesmen_results);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof TradesmenResultsInteractionListener) {
            mListener = (TradesmenResultsInteractionListener) context;
            mListener.setDoneBtnClickListener(this);
        } else {
            throw new IllegalArgumentException("context must implement "
                        + TradesmenResultsInteractionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener.setDoneBtnClickListener(null);
        mListener = null;
    }

    @Override
    public void onTradesmanClick(int adapterPosition, TradesmanWrapper tradesman) {
        if(mListener != null) {
            mListener.showTradesman(adapterPosition, tradesman);
        }
    }

    @Override
    public void onTradesmanUnselected(boolean hasMoreSelections) {
        if(mListener != null && !hasMoreSelections) {
            setToolbarTitle(getString(R.string.no_tradesmen_selected));
            mListener.hideDoneBtn();
        } else if(hasMoreSelections) {
            setToolbarTitle(getString(
                    R.string.selected_x_out_of_tradesmen,
                    mAdapter.getSelectedItemCount(),
                    mMaxTradesmanSelection
            ));
        }
    }

    public void setTradesmen(List<Tradesman> tradesmen, Map<String, Integer> reviewCountForTradesmen) {
        if(tradesmen != null && !tradesmen.isEmpty()) {
            List<TradesmanWrapper> adapterData = new ArrayList<>();
            for (Tradesman tradesman : tradesmen) {
                adapterData.add(new TradesmanWrapper(tradesman, reviewCountForTradesmen.get(tradesman.get_id())));
            }
            mAdapter.setTradesmen(adapterData);
        }
    }

    public void selectTradesman(int adapterPosition) {
        if(mListener != null) {
            if (!mAdapter.selectTradesman(adapterPosition)) {
                notifyUser(getString(R.string.format_tradesmen_selection_limit, mMaxTradesmanSelection));
            } else {
                setToolbarTitle(getString(
                    R.string.selected_x_out_of_tradesmen,
                    mAdapter.getSelectedItemCount(),
                    mMaxTradesmanSelection
                ));
                mListener.showDoneBtn();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null) {
            mListener.orderTradesmen(mAdapter.getSelectedTradesmen());
        }
    }

    public interface TradesmenResultsInteractionListener {
        void showTradesman(int fromAdapterPosition, TradesmanWrapper tradesman);
        void orderTradesmen(List<TradesmanWrapper> selectedTradesmen);

        void showDoneBtn();
        void hideDoneBtn();
        void setDoneBtnClickListener(View.OnClickListener onClickListener);
    }
}
