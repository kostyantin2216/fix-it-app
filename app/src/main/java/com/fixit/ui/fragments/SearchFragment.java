package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
import com.fixit.utils.Constants;
import com.fixit.utils.DataUtils;
import com.fixit.external.google.GoogleClientManager;
import com.fixit.ui.adapters.PlaceAutocompleteAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;

/**
 * Created by konstantin on 3/29/2017.
 */

public class SearchFragment extends BaseFragment<SearchController>
        implements View.OnClickListener,
                   GoogleClientManager.GooglePlacesCallback {

    private SearchFragmentInteractionListener mListener;

    private CoordinatorLayout root;
    private AutoCompleteTextView actvProfessions;
    private AutoCompleteTextView actvAddress;



    public static SearchFragment newInstance(@Nullable String defaultProfession, @Nullable String defaultAddress) {
        SearchFragment frag = new SearchFragment();
        Bundle args = new Bundle();
        if(!TextUtils.isEmpty(defaultProfession)) {
            args.putString(Constants.ARG_DEFAULT_PROFESSION, defaultProfession);
        }
        if(!TextUtils.isEmpty(defaultAddress)) {
            args.putString(Constants.ARG_DEFAULT_ADDRESS, defaultAddress);
        }
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        root = (CoordinatorLayout) v.findViewById(R.id.root);
        actvProfessions = (AutoCompleteTextView) v.findViewById(R.id.actv_professions);
        actvAddress = (AutoCompleteTextView) v.findViewById(R.id.actv_address);

        actvAddress.setOnItemClickListener((parent, view, position, id) -> hideKeyboard(root));
        actvProfessions.setOnItemClickListener((parent, view, position, id) -> {
            if(!actvAddress.getText().toString().trim().isEmpty()) {
                hideKeyboard(root);
            }
        });

        v.findViewById(R.id.btn_show_professions).setOnClickListener(this);
        v.findViewById(R.id.btn_show_map).setOnClickListener(this);
        v.findViewById(R.id.fab_search).setOnClickListener(this);

        setToolbar((Toolbar) v.findViewById(R.id.toolbar));

       // FontCache.setTypefaceRecursive(v, FontCache.CachedTypeface.STANDARD);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Profession[] professions = getController().getProfessions();
        actvProfessions.setAdapter(new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1,
                DataUtils.toAutoCompleteList(professions)
        ));

        checkDefaults();
    }

    private void checkDefaults() {
        Bundle args = getArguments();
        if(args != null) {
            if (args.containsKey(Constants.ARG_DEFAULT_PROFESSION)) {
                actvProfessions.setText(args.getString(Constants.ARG_DEFAULT_PROFESSION));
            }
            if (args.containsKey(Constants.ARG_DEFAULT_ADDRESS)) {
                actvAddress.setText(args.getString(Constants.ARG_DEFAULT_ADDRESS));
            }
        }
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {

        actvAddress.setAdapter(new PlaceAutocompleteAdapter(getContext(), googleApiClient, null,
                new AutocompleteFilter
                        .Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                        .setCountry("ZA")
                        .build()
        ));
    }

    @Override
    public void onPlaceChosen(Place place) {
        actvAddress.setText(place.getAddress());
    }

    public void onProfessionChosen(Profession profession) {
        actvProfessions.setText(profession.getName());
        actvProfessions.dismissDropDown();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof SearchFragmentInteractionListener) {
            this.mListener = (SearchFragmentInteractionListener) context;
        } else {
            throw new IllegalArgumentException("context does not implement "
                    + SearchFragmentInteractionListener.class.getName());
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
            clearFocus();
            switch (v.getId()) {
                case R.id.btn_show_professions:
                    mListener.showProfessionsList();
                    break;
                case R.id.btn_show_map:
                    mListener.showMap();
                    break;
                case R.id.fab_search:
                    String profession = actvProfessions.getText().toString();
                    if(TextUtils.isEmpty(profession)) {
                        notifyUser(getString(R.string.empty_profession), root);
                    } else {
                        String address = actvAddress.getText().toString();
                        if(TextUtils.isEmpty(address)) {
                            notifyUser(getString(R.string.empty_address), root);
                        } else {
                            mListener.performSearch(profession, address);
                        }
                    }
                    break;
            }
        }
    }

    private void clearFocus() {
        actvProfessions.clearFocus();
        actvAddress.clearFocus();
    }

    public interface SearchFragmentInteractionListener {
        void showProfessionsList();
        void showMap();
        void performSearch(String profession, String address);
    }

}
