package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.ProfessionRecyclerAdapter;
import com.fixit.app.ui.adapters.SearchableStaticRecyclerListFragment;
import com.fixit.core.controllers.SearchController;
import com.fixit.core.data.Profession;
import com.fixit.core.ui.adapters.CommonRecyclerAdapter;
import com.fixit.core.ui.fragments.StaticRecyclerListFragment;

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
            mListener.onProfessionSelect(item);
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
        void onProfessionSelect(Profession profession);
    }

}
