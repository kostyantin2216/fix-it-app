package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.JobReasonsAdapter;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.JobReason;
import com.fixit.core.ui.fragments.StaticRecyclerListFragment;
import com.fixit.core.utils.Constants;

import java.util.List;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class JobReasonsSelectionFragment extends StaticRecyclerListFragment<OrderController>
        implements OrderController.JobReasonsCallback, View.OnClickListener {

    private JobReasonsInteractionListener mListener;
    private JobReasonsAdapter mAdapter;

    public static JobReasonsSelectionFragment newInstance(int professionId) {
        JobReasonsSelectionFragment fragment = new JobReasonsSelectionFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PROFESSION_ID, professionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_coordinated_recycler_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setBackground(R.drawable.bg_brick_wall);
        setToolbar((Toolbar) view.findViewById(R.id.toolbar), true);
        getController().findReasonsForProfession(getProfessionId(), this);
    }

    private int getProfessionId() {
        return getArguments().getInt(Constants.ARG_PROFESSION_ID);
    }

    @Override
    public void onReceiveJobReasons(List<JobReason> reasons) {
        mAdapter = JobReasonsAdapter.create(getContext(), reasons);
        setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null) {
            mListener.onJobReasonsSelected(mAdapter.getSelectedJobReasons());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof JobReasonsInteractionListener) {
            mListener = (JobReasonsInteractionListener) context;
        } else {
            throw new IllegalArgumentException("context needs to implement " + JobReasonsInteractionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public interface JobReasonsInteractionListener {
        void onJobReasonsSelected(List<JobReason> reasons);
    }
}
