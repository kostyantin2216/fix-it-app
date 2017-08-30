package com.fixit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fixit.app.R;
import com.fixit.controllers.OrderController;

/**
 * Created by Kostyantin on 5/30/2017.
 */

public class OrderCompletionFragment extends BaseFragment<OrderController> implements View.OnClickListener {

    private ViewHolder mView;

    public static Fragment newInstance() {
        return new OrderCompletionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_completion, container, false);

        mView = new ViewHolder(v, this);
        mView.setState(ViewState.LOADING);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_tradesmen:
                getActivity().onBackPressed();
                break;
            case R.id.btn_new_search:
                restartApp(true);
                break;
            case R.id.btn_exit_app:
                getActivity().finishAffinity();
                break;
        }
    }

    public void onOrderComplete() {
        mView.setState(ViewState.COMPLETE);
    }

    private enum ViewState {
        LOADING,
        COMPLETE
    }

    private static class ViewHolder {
        final ViewGroup contentContainer;
        final ProgressBar loader;

        ViewHolder(View v, View.OnClickListener onClickListener) {
            contentContainer = (ViewGroup) v.findViewById(R.id.container);
            loader = (ProgressBar) v.findViewById(R.id.loader);

            v.findViewById(R.id.btn_view_tradesmen).setOnClickListener(onClickListener);
            v.findViewById(R.id.btn_new_search).setOnClickListener(onClickListener);
            v.findViewById(R.id.btn_exit_app).setOnClickListener(onClickListener);
        }

        void setState(ViewState state) {
            switch (state) {
                case LOADING:
                    contentContainer.setVisibility(View.INVISIBLE);
                    loader.setVisibility(View.VISIBLE);
                    break;
                case COMPLETE:
                    contentContainer.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    break;
            }
        }

    }

}
