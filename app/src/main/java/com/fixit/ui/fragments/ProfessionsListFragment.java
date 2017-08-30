package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fixit.app.R;
import com.fixit.controllers.SearchController;
import com.fixit.data.Profession;
import com.fixit.ui.adapters.CommonRecyclerAdapter;
import com.fixit.ui.adapters.ProfessionRecyclerAdapter;
import com.fixit.ui.adapters.SearchableStaticRecyclerListFragment;

/**
 * Created by konstantin on 4/2/2017.
 */

public class ProfessionsListFragment extends SearchableStaticRecyclerListFragment<SearchController> implements CommonRecyclerAdapter.CommonRecyclerViewInteractionListener<Profession> {

    private ProfessionSelectionListener mListener;
    private ProfessionRecyclerAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchController searchController = getController();
        assert  searchController != null;
        Profession[] professions = searchController.getProfessions();
        mAdapter = new ProfessionRecyclerAdapter(professions, this);
        setAdapter(mAdapter);

        setBackground(R.drawable.bg_corner_nuts);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ProfessionSelectionListener) {
            mListener = (ProfessionSelectionListener) context;
        } else {
            throw new IllegalArgumentException("Context must implement "
                    + ProfessionSelectionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder vh, Profession item) {
        if(mListener != null) {
            mListener.onProfessionSelected(item);
        }
    }

    @Override
    public void onTextChanged(final String text) {
        mAdapter.filter(new CommonRecyclerAdapter.AdapterFilterer<Profession>() {
            @Override
            public boolean filter(Profession data) {
                return !data.getName().trim().toLowerCase().contains(text.toLowerCase());
            }
        });
    }

    public interface ProfessionSelectionListener {
        void onProfessionSelected(Profession profession);
    }

}
