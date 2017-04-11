package com.fixit.core.ui.fragments;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fixit.core.R;
import com.fixit.core.controllers.ActivityController;

/**
 * Created by konstantin on 4/2/2017.
 */

public abstract class StaticRecyclerListFragment<C extends ActivityController> extends BaseFragment<C> {

    private ViewGroup root;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_recycler_list, container, false);

        root = (ViewGroup) v.findViewById(R.id.root);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        return v;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter, new LinearLayoutManager(getContext()));
    }

    public void setAdapter(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setBackground(@DrawableRes int resId) {
        root.setBackgroundResource(resId);
    }

}
