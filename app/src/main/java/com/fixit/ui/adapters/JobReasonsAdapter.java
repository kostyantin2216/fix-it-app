package com.fixit.ui.adapters;

import android.content.Context;
import android.util.SparseArray;

import com.fixit.config.AppConfig;
import com.fixit.data.JobReason;
import com.fixit.ui.adapters.animations.RecyclerItemSlideInAnimation;

import java.util.List;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class JobReasonsAdapter extends MultiSelectRecyclerAdapter {

    public static JobReasonsAdapter create(Context context, JobReason[] jobReasons) {
        int jobReasonCount = jobReasons.length;
        SelectItem[] selectItems = new SelectItem[jobReasonCount];
        SparseArray<JobReason> items = new SparseArray<>();
        for(int i = 0; i < jobReasonCount; i++) {
            JobReason jobReason = jobReasons[i];
            int id = (int) jobReason.getId();
            selectItems[i] = new SelectItem(id, jobReason.getName());
            items.put(id, jobReason);
        }

        return new JobReasonsAdapter(context, items, selectItems);
    }

    private final SparseArray<JobReason> mJobReasons;

    private JobReasonsAdapter(Context context, SparseArray<JobReason> items, SelectItem[] selectItems) {
        super(
                new RecyclerItemSlideInAnimation(context, RecyclerItemSlideInAnimation.Direction.RIGHT),
                selectItems,
                AppConfig.getInteger(context, AppConfig.KEY_MAX_JOB_REASON_SELECTION, -1)
        );

        mJobReasons = items;
    }

    @Override
    protected int getLayoutRes() {
        return com.fixit.app.R.layout.list_item_multiselect_dark;
    }

    public JobReason[] getSelectedJobReasons() {
        SelectItem[] selectedItems = getSelectedItems();
        JobReason[] selectedReasons = new JobReason[selectedItems.length];
        for(int i = 0; i < selectedItems.length; i++) {
            selectedReasons[i] = mJobReasons.get(selectedItems[i].code);
        }
        return selectedReasons;
    }

    public void setSelectedJobReasons(JobReason[] jobReasons) {
        SelectItem[] selectedItems = new SelectItem[jobReasons.length];
        for(int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = new SelectItem((int) jobReasons[i].getId(), jobReasons[i].getName());
        }
        setSelectedItems(selectedItems);
    }
}
