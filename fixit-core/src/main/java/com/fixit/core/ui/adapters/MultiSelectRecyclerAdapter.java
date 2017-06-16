package com.fixit.core.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fixit.core.R;
import com.fixit.core.ui.adapters.MultiSelectRecyclerAdapter.SelectItem;
import com.fixit.core.ui.adapters.MultiSelectRecyclerAdapter.SelectItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class MultiSelectRecyclerAdapter extends CommonRecyclerAdapter<SelectItem, SelectItemViewHolder> implements View.OnClickListener {

    public MultiSelectRecyclerAdapter(Context context, List<SelectItem> items) {
        super(context, items);
    }

    public List<SelectItem> getSelectedItems() {
        List<SelectItem> selectedItems = new ArrayList<>();
        List<SelectItem> selectItems = getItems();
        for(SelectItem item : selectItems) {
            if(item.isSelected) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    protected int getLayoutRes() {
        return R.layout.list_item_multiselect;
    }

    @Override
    public SelectItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutRes(), viewGroup, false);
        SelectItemViewHolder viewHolder = new SelectItemViewHolder(v);
        v.setOnClickListener(this);
        v.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onClick(View v) {
        SelectItemViewHolder viewHolder = (SelectItemViewHolder) v.getTag();
        int adapterPos = viewHolder.getAdapterPosition();
        SelectItem selectItem = getItem(adapterPos);
        selectItem.isSelected = !selectItem.isSelected;
        notifyItemChanged(adapterPos);
    }

    public static class SelectItem {
        public final int code;
        public final String display;
        private boolean isSelected;

        public SelectItem(int code, String display) {
            this.code = code;
            this.display = display;
            this.isSelected = false;
        }
    }

    static class SelectItemViewHolder extends CommonRecyclerAdapter.CommonViewHolder<SelectItem> {

        final TextView tvDisplay;
        final CheckBox cbSelected;

        public SelectItemViewHolder(View itemView) {
            super(itemView);

            tvDisplay = (TextView) itemView.findViewById(R.id.tv_display);
            cbSelected = (CheckBox) itemView.findViewById(R.id.cb_selected);
        }

        @Override
        public void populate(SelectItem entity) {
            tvDisplay.setText(entity.display);
            cbSelected.setChecked(entity.isSelected);
        }
    }
}
