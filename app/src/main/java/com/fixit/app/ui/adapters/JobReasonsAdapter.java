package com.fixit.app.ui.adapters;

import android.content.Context;

import com.fixit.core.data.JobReason;
import com.fixit.core.ui.adapters.MultiSelectRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class JobReasonsAdapter extends MultiSelectRecyclerAdapter {

    public static JobReasonsAdapter create(Context context, List<JobReason> jobReasons) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<Integer, JobReason> items = new HashMap<>();
        for(JobReason jobReason : jobReasons) {
            Integer id = jobReason.getId();
            selectItems.add(new SelectItem(jobReason.getId(), jobReason.getName()));
            items.put(id, jobReason);
        }
        return new JobReasonsAdapter(context, items, selectItems);
    }

    private final Map<Integer, JobReason> mJobReasons;

    private JobReasonsAdapter(Context context, Map<Integer, JobReason> items, List<SelectItem> selectItems) {
        super(context, selectItems);

        mJobReasons = items;
    }

    @Override
    protected int getLayoutRes() {
        return com.fixit.core.R.layout.list_item_multiselect_dark;
    }

    public List<JobReason> getSelectedJobReasons() {
        List<JobReason> selectedReasons = new ArrayList<>();
        List<SelectItem> selectedItems = getSelectedItems();
        for(SelectItem item : selectedItems) {
            selectedReasons.add(mJobReasons.get(item.code));
        }
        return selectedReasons;
    }

}
