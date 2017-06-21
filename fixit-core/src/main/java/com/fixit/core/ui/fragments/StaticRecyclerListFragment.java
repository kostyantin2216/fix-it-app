package com.fixit.core.ui.fragments;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixit.core.R;
import com.fixit.core.controllers.ActivityController;

/**
 * Created by konstantin on 4/2/2017.
 */

public abstract class StaticRecyclerListFragment<C extends ActivityController> extends BaseFragment<C> {

    private ViewManager mViewManager;

    @Override
    public void onResume() {
        super.onResume();
        mViewManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewManager.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResId(), container, false);

        mViewManager = new ViewManager(v, getEmptyListMessage());

        return v;
    }

    public int getLayoutResId() {
        return R.layout.layout_recycler_list;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter, new LinearLayoutManager(getContext()));
    }

    public void setAdapter(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        mViewManager.setAdapter(adapter, layoutManager);
    }

    public RecyclerView.Adapter getAdapter() {
        return mViewManager.getAdapter();
    }

    public String getEmptyListMessage() {
        return getString(R.string.empty_list);
    }

    public void setBackground(@DrawableRes int resId) {
        mViewManager.root.setBackgroundResource(resId);
    }

    public void setAppBarToolBar(@LayoutRes int appBarToolBarRes, @IdRes int toolbarResId) {
        AppBarLayout appBar = (AppBarLayout) LayoutInflater.from(getContext()).inflate(appBarToolBarRes, mViewManager.root, false);
        mViewManager.setAppBarToolBar(appBar);
        super.setToolbar((Toolbar) appBar.findViewById(toolbarResId));
    }

    public void setToolbar(@LayoutRes int toolbarRes) {
        Toolbar toolbar = (Toolbar) LayoutInflater.from(getContext()).inflate(toolbarRes, mViewManager.root, false);
        mViewManager.setToolbar(toolbar);
        super.setToolbar(toolbar);
    }

    public void setToolbar(Toolbar toolbar) {
        mViewManager.setToolbar(toolbar);
        super.setToolbar(toolbar);
    }

    private static class ViewManager extends RecyclerView.AdapterDataObserver {
        final LinearLayout root;
        final RecyclerView recyclerView;
        final TextView tvEmptyList;

        ViewManager(View v, String emptyListMessage) {
            root = (LinearLayout) v.findViewById(R.id.root);
            recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
            tvEmptyList = (TextView) v.findViewById(R.id.tv_empty_list);

            tvEmptyList.setText(emptyListMessage);
        }

        void setAdapter(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
            adapter.registerAdapterDataObserver(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);

            updateListStatus();
        }

        RecyclerView.Adapter getAdapter() {
            return recyclerView.getAdapter();
        }

        void onResume() {
            RecyclerView.Adapter adapter = getAdapter();
            if(adapter != null) {
                try {
                    adapter.registerAdapterDataObserver(this);
                } catch (IllegalStateException e) {
                    // do nothing, observer was already registered when adapter was set.
                }
            }
        }

        void onPause() {
            RecyclerView.Adapter adapter = getAdapter();
            if(adapter != null) {
                adapter.unregisterAdapterDataObserver(this);
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateListStatus();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateListStatus();
        }

        void updateListStatus() {
            RecyclerView.Adapter adapter = getAdapter();
            if(adapter != null && adapter.getItemCount() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyList.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                tvEmptyList.setVisibility(View.VISIBLE);
            }
        }

        void setToolbar(Toolbar toolbar) {
            root.addView(toolbar, 0);
        }

        void setAppBarToolBar(AppBarLayout appBarToolBar) {
            root.addView(appBarToolBar, 0);
        }
        
    }

}
