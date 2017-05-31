package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.TradesmenAdapter;
import com.fixit.app.ui.styleholders.ButtonStyleHolder;
import com.fixit.core.config.AppConfig;
import com.fixit.core.controllers.ResultsController;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.ui.fragments.StaticRecyclerListFragment;
import com.fixit.core.ui.styleholders.StyleHolder;
import com.fixit.core.utils.UIUtils;

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
    private Button btnContact;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMaxTradesmanSelection = AppConfig.getInt(getContext(), AppConfig.KEY_MAX_TRADESMEN_SELECTION, 3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();
        btnContact = new Button(context);
        btnContact.setText(getString(R.string.contact_selected_tradesmen));
        btnContact.setOnClickListener(this);

        StyleHolder<Button> styleHolder = new ButtonStyleHolder(context);
        styleHolder.applyStyle(btnContact);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int margin = UIUtils.dpToPx(context, 12);
        lp.setMargins(margin, 0, margin, margin);

        UIUtils.collapse(btnContact, 1);
        v.addView(btnContact, lp);

        return v;
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
    public void onTradesmanSelected(int adapterPosition, TradesmanWrapper tradesman) {
        if(mListener != null) {
            mListener.showTradesman(adapterPosition, tradesman);
        }
    }

    @Override
    public void onTradesmanUnselected(boolean hasMoreSelections) {
        if(!hasMoreSelections) {
            UIUtils.collapse(btnContact, 5);
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
        if(!mAdapter.selectTradesman(adapterPosition)) {
            notifyUser(getString(R.string.format_tradesmen_selection_limit,  mMaxTradesmanSelection));
        } else if(btnContact.getVisibility() != View.VISIBLE) {
            btnContact.postDelayed(new Runnable() {
                @Override
                public void run() {
                    UIUtils.expand(btnContact, 5);
                }
            }, 200);
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null && v.getId() == btnContact.getId()) {
            mListener.orderTradesmen(mAdapter.getSelectedTradesmen());
        }
    }

    public interface TradesmenResultsInteractionListener {
        void showTradesman(int fromAdapterPosition, TradesmanWrapper tradesman);
        void orderTradesmen(List<TradesmanWrapper> selectedTradesmen);
    }
}
