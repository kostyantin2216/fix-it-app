package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.fixit.app.R;
import com.fixit.controllers.SearchController;
import com.fixit.data.Profession;
import com.fixit.ui.adapters.CommonRecyclerAdapter;
import com.fixit.ui.adapters.ProfessionRecyclerAdapter;
import com.fixit.utils.Constants;
import com.fixit.utils.DataUtils;

/**
 * Created by Kostyantin on 10/28/2017.
 */

public class ProfessionPickerFragment extends BaseFragment<SearchController> implements CommonRecyclerAdapter.CommonRecyclerViewInteractionListener<Profession>, AdapterView.OnItemClickListener {

    private ProfessionSelectionListener mListener;
    private ProfessionRecyclerAdapter mAdapter;

    private ViewHolder mView;

    private static class ViewHolder {
        final AutoCompleteTextView actvProfessions;
        final RecyclerView rvProfessions;

        ViewHolder(View v, AdapterView.OnItemClickListener professionItemClickListener) {
            actvProfessions = (AutoCompleteTextView) v.findViewById(R.id.actv_professions);
            actvProfessions.setOnItemClickListener(professionItemClickListener);

            rvProfessions = (RecyclerView) v.findViewById(R.id.rv_professions);
            rvProfessions.requestFocus();

            rvProfessions.setLayoutManager(new LinearLayoutManager(v.getContext()));
        }

        public void updateDefaults(Bundle args) {
            if(args != null) {
                String defaultProfession = args.getString(Constants.ARG_DEFAULT_PROFESSION);
                if(!TextUtils.isEmpty(defaultProfession)) {
                    actvProfessions.setText(defaultProfession);
                }
            }
        }
    }

    public static ProfessionPickerFragment newInstance(String defaultProfession) {
        ProfessionPickerFragment fragment = new ProfessionPickerFragment();

        Bundle args = new Bundle();
        args.putString(Constants.ARG_DEFAULT_PROFESSION, defaultProfession);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profession_picker, container, false);

        mView = new ViewHolder(v, this);
        setToolbar((Toolbar) v.findViewById(R.id.toolbar));

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchController searchController = getController();
        assert  searchController != null;
        Profession[] professions = searchController.getProfessions();
        mAdapter = new ProfessionRecyclerAdapter(professions, this);
        mView.rvProfessions.setAdapter(mAdapter);
        mView.actvProfessions.setAdapter(new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1,
                DataUtils.toAutoCompleteList(professions)
        ));

        mView.updateDefaults(getArguments());
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
        mListener.onProfessionSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onProfessionSelected(mAdapter.getItem(position));
    }

    public interface ProfessionSelectionListener {
        void onProfessionSelected(Profession profession);
    }
}
