package com.fixit.ui.fragments;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.Button;

import com.fixit.app.R;
import com.fixit.controllers.SearchController;
import com.fixit.data.Profession;
import com.fixit.ui.helpers.UITutorials;
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

    private ViewHolder mView;

    private static class ViewHolder {

        private final CoordinatorLayout root;
        private final AutoCompleteTextView actvProfessions;
        private final AutoCompleteTextView actvAddress;

        private final Button btnProfessions;
        private final Button btnMap;
        private final Button btnSearch;

        private final AnimationDrawable animGradient;

        ViewHolder(View v, View.OnClickListener clickListener, AdapterView.OnItemClickListener addressItemClickListener, AdapterView.OnItemClickListener professionItemClickListener) {
            root = (CoordinatorLayout) v.findViewById(R.id.root);
            actvProfessions = (AutoCompleteTextView) v.findViewById(R.id.actv_professions);
            actvAddress = (AutoCompleteTextView) v.findViewById(R.id.actv_address);

            actvAddress.setOnItemClickListener(addressItemClickListener);
            actvProfessions.setOnItemClickListener(professionItemClickListener);

            btnProfessions = (Button) v.findViewById(R.id.btn_show_professions);
            btnMap = (Button) v.findViewById(R.id.btn_show_map);
            btnSearch = (Button) v.findViewById(R.id.fab_search);

            btnSearch.setBackgroundResource(R.drawable.rounded_gradient_animation_list);
            animGradient = (AnimationDrawable) btnSearch.getBackground();
            animGradient.start();
            animGradient.setEnterFadeDuration(2000);
            animGradient.setExitFadeDuration(2000);

            btnProfessions.setOnClickListener(clickListener);
            btnMap.setOnClickListener(clickListener);
            btnSearch.setOnClickListener(clickListener);
        }

        String getAddress() {
            return actvAddress.getText().toString();
        }

        void setAddress(Place place) {
            actvAddress.setText(place.getAddress());
        }

        String getProfession() {
            return actvProfessions.getText().toString();
        }

        void setProfession(Profession profession) {
            actvProfessions.setText(profession.getName());
            actvProfessions.dismissDropDown();
        }

        void update(Bundle args) {
            if (args.containsKey(Constants.ARG_DEFAULT_PROFESSION)) {
                actvProfessions.setText(args.getString(Constants.ARG_DEFAULT_PROFESSION));
            }
            if (args.containsKey(Constants.ARG_DEFAULT_ADDRESS)) {
                actvAddress.setText(args.getString(Constants.ARG_DEFAULT_ADDRESS));
            }
        }

        void clearFocus() {
            actvProfessions.clearFocus();
            actvAddress.clearFocus();
        }
    }

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

        mView = new ViewHolder(v, this,
                (parent, view, position, id) -> hideKeyboard(mView.root),
                (parent, view, position, id) -> {
            if(!mView.actvAddress.getText().toString().trim().isEmpty()) {
                hideKeyboard(mView.root);
            }
        });

        setToolbar((Toolbar) v.findViewById(R.id.toolbar));

       // FontCache.setTypefaceRecursive(v, FontCache.CachedTypeface.STANDARD);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Profession[] professions = getController().getProfessions();
        mView.actvProfessions.setAdapter(new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1,
                DataUtils.toAutoCompleteList(professions)
        ));

        checkDefaults();

        UITutorials.create(UITutorials.TUTORIAL_SEARCH_SCREEN, mView.btnProfessions, getString(R.string.tutorial_show_professions))
                .and(mView.btnMap, getString(R.string.tutorial_show_map))
                .and(mView.btnSearch, getString(R.string.tutorial_search))
                .show(getFragmentManager());
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mView != null && mView.animGradient != null) {
            mView.animGradient.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mView.animGradient.stop();
    }

    private void checkDefaults() {
        Bundle args = getArguments();
        if(args != null) {
            mView.update(args);
        }
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mView.actvAddress.setAdapter(new PlaceAutocompleteAdapter(getContext(), googleApiClient, null,
                new AutocompleteFilter
                        .Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                        .setCountry("ZA")
                        .build()
        ));
    }

    @Override
    public void onPlaceChosen(Place place) {
        mView.setAddress(place);
    }

    public void onProfessionChosen(Profession profession) {
        mView.setProfession(profession);
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
            mView.clearFocus();
            switch (v.getId()) {
                case R.id.btn_show_professions:
                    mListener.showProfessionsList();
                    break;
                case R.id.btn_show_map:
                    mListener.showMap();
                    break;
                case R.id.fab_search:
                    String profession = mView.getProfession();
                    if(TextUtils.isEmpty(profession)) {
                        notifyUser(getString(R.string.empty_profession), mView.root);
                    } else {
                        String address = mView.getAddress();
                        if(TextUtils.isEmpty(address)) {
                            notifyUser(getString(R.string.empty_address), mView.root);
                        } else {
                            mListener.performSearch(profession, address);
                        }
                    }
                    break;
            }
        }
    }

    public interface SearchFragmentInteractionListener {
        void showProfessionsList();
        void showMap();
        void performSearch(String profession, String address);
    }

}
