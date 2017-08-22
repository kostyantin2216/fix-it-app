package com.fixit.app.ui.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.fixit.app.R;
import com.fixit.core.controllers.ActivityController;
import com.fixit.core.ui.fragments.StaticRecyclerListFragment;

/**
 * Created by Kostyantin on 6/23/2017.
 */

public abstract class SearchableStaticRecyclerListFragment<C extends ActivityController> extends StaticRecyclerListFragment<C>
        implements TextWatcher {

    private EditText etSearch;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppBarLayout appBarLayout = setAppBarToolBar(R.layout.layout_appbar_search_toolbar, R.id.toolbar, true);
        etSearch = (EditText) appBarLayout.findViewById(R.id.et_search);
        etSearch.setVisibility(View.VISIBLE);

        getRecyclerView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.clearFocus();
                hideKeyboard(getView());
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        etSearch.addTextChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        hideKeyboard(getView());
        etSearch.removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        onTextChanged(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) { }

    public abstract void onTextChanged(String text);

}
