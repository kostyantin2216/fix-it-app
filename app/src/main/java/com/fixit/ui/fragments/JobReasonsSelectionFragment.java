package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fixit.app.R;
import com.fixit.controllers.OrderController;
import com.fixit.data.JobReason;
import com.fixit.ui.adapters.CommonRecyclerAdapter;
import com.fixit.ui.adapters.MultiSelectRecyclerAdapter;
import com.fixit.ui.components.FloatingTextButton;
import com.fixit.utils.Constants;
import com.fixit.ui.adapters.JobReasonsAdapter;
import com.fixit.ui.adapters.SearchableStaticRecyclerListFragment;

import java.util.List;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class JobReasonsSelectionFragment extends SearchableStaticRecyclerListFragment<OrderController>
        implements OrderController.JobReasonsCallback,
                   JobReasonsAdapter.OnMultiSelectItemChangeListener,
                   View.OnClickListener {

    private JobReasonsInteractionListener mListener;
    private JobReasonsAdapter mAdapter;

    private FloatingTextButton fabDone;

    public static JobReasonsSelectionFragment newInstance(long professionId) {
        JobReasonsSelectionFragment fragment = new JobReasonsSelectionFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ARG_PROFESSION_ID, professionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_done_recycler_view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        assert v != null;
        fabDone = (FloatingTextButton) v.findViewById(R.id.fab_done);
        fabDone.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideKeyboard(view);

        setBackground(R.drawable.bg_brick_wall);
        awaitData();

        OrderController controller = getController();
        assert controller != null;
        getController().findReasonsForProfession(getProfessionId(), this);
    }

    @Override
    public void onTextChanged(String text) {
        if(mAdapter != null)
            mAdapter.filter(data -> !data.display.toLowerCase().contains(text.trim().toLowerCase()));
    }

    private long getProfessionId() {
        return getArguments().getLong(Constants.ARG_PROFESSION_ID);
    }

    @Override
    public void onReceiveJobReasons(JobReason[] reasons) {
        mAdapter = JobReasonsAdapter.create(getContext(), reasons);
        mAdapter.setSelectItemChangeListener(this);
        if(mListener != null) {
            mAdapter.setSelectedJobReasons(mListener.getSelectedJobReasons());
            invalidateDoneBtn();
        }
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
            invalidateSelectedJobReasons();
        } else {
            throw new IllegalArgumentException("context needs to implement " + JobReasonsInteractionListener.class.getName());
        }
    }

    private void invalidateSelectedJobReasons() {
        if(mAdapter != null) {
            JobReason[] selectedJobReasons = mListener.getSelectedJobReasons();
            if(selectedJobReasons.length != mAdapter.getSelectedItemCount()) {
                mAdapter.setSelectedJobReasons(selectedJobReasons);
                invalidateDoneBtn();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onSelectItemChanged(MultiSelectRecyclerAdapter.SelectItem selectItem) {
        invalidateDoneBtn();
    }

    @Override
    public void onSelectionLimitReached(int limit) {
        notifyUser(getString(R.string.cant_select_gt_x_reasons, limit));
    }

    private void invalidateDoneBtn() {
       // if(mAdapter.getSelectedItemCount() > 0) {
            fabDone.setVisibility(View.VISIBLE);
       /* } else {
            fabDone.setVisibility(View.GONE);
        }*/
    }

    public interface JobReasonsInteractionListener {
        void onJobReasonsSelected(JobReason[] reasons);
        JobReason[] getSelectedJobReasons();
    }
}
