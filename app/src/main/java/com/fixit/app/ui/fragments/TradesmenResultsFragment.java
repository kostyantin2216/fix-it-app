package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.TradesmenAdapter;
import com.fixit.core.controllers.ResultsController;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.ui.fragments.StaticRecyclerListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by konstantin on 4/3/2017.
 */
public class TradesmenResultsFragment extends StaticRecyclerListFragment<ResultsController>
    implements TradesmenAdapter.TradesmanAdapterCallback {

    private TradesmenResultsInteractionListener mListener;
    private TradesmenAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setBackground(R.drawable.bg_brick_wall);

        mAdapter = new TradesmenAdapter(getContext(), this);
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
        } else {
            throw new IllegalArgumentException("context must implement "
                        + TradesmenResultsInteractionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onTradesmanSelected(TradesmanWrapper tradesman) {
        if(mListener != null) {
            mListener.onTradesmanSelected(tradesman);
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

    public List<Tradesman> getSelectedTradesmen() {
        return mAdapter.getSelectedTradesmen();
    }

    public interface TradesmenResultsInteractionListener {
        void onTradesmanSelected(TradesmanWrapper tradesman);
    }
}
