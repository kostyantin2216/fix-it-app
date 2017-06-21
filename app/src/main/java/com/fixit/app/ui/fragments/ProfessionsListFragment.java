package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.ProfessionRecyclerAdapter;
import com.fixit.core.controllers.SearchController;
import com.fixit.core.data.Profession;
import com.fixit.core.ui.adapters.CommonRecyclerAdapter;
import com.fixit.core.ui.fragments.StaticRecyclerListFragment;

/**
 * Created by konstantin on 4/2/2017.
 */

public class ProfessionsListFragment extends StaticRecyclerListFragment<SearchController> implements CommonRecyclerAdapter.CommonRecyclerViewInteractionListener<Profession> {

    private ProfessionSelectionListener mListener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchController searchController = getController();
        assert  searchController != null;
        Profession[] professions = searchController.getProfessions();
        ProfessionRecyclerAdapter adapter = new ProfessionRecyclerAdapter(professions, this);
        setAdapter(adapter);

        setAppBarToolBar(R.layout.layout_appbar_toolbar, R.id.toolbar);
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
    public void onItemClick(Profession item) {
        if(mListener != null) {
            mListener.onProfessionSelect(item);
        }
    }

    public interface ProfessionSelectionListener {
        void onProfessionSelect(Profession profession);
    }

}
