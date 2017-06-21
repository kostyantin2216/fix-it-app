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
import com.fixit.core.ui.adapters.animations.RecyclerItemAnimation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class MultiSelectRecyclerAdapter extends CommonRecyclerAdapter<SelectItem, SelectItemViewHolder> implements View.OnClickListener {

    private OnMultiSelectItemChangeListener mChangeListener;
    private Set<Integer> mSelectedItemPositions = new HashSet<>();

    private final int mMaxSelection;

    public MultiSelectRecyclerAdapter(RecyclerItemAnimation itemAnimation, SelectItem[] items, int maxSelection) {
        super(itemAnimation, items, null);
        this.mMaxSelection = maxSelection;
    }

    public MultiSelectRecyclerAdapter(Context context, SelectItem[] items, int maxSelection) {
        super(context, items);
        this.mMaxSelection = maxSelection;
    }

    public MultiSelectRecyclerAdapter(RecyclerItemAnimation itemAnimation, SelectItem[] items) {
        this(itemAnimation, items, -1);
    }

    public MultiSelectRecyclerAdapter(Context context, SelectItem[] items) {
        this(context, items, -1);
    }

    public void setSelectItemChangeListener(OnMultiSelectItemChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    public int getSelectedItemCount() {
        return mSelectedItemPositions.size();
    }

    public void setSelectedItems(SelectItem[] selectedItemsArg) {
        SelectItem[] selectedItems = Arrays.copyOf(selectedItemsArg, selectedItemsArg.length);
        SelectItem[] adapterItems = getItems();

        mSelectedItemPositions.clear();
        for(int ai = 0; ai < adapterItems.length; ai++) {
            boolean selected = false;

            for(int si = 0; si < selectedItems.length; si++) {
                if(selectedItems[si] != null && selectedItems[si].code == adapterItems[ai].code) {
                    selectedItems[si] = null;
                    selected = true;
                    break;
                }
            }

            if(!selected) {
                adapterItems[ai].isSelected = false;
            } else {
                adapterItems[ai].isSelected = true;
                mSelectedItemPositions.add(ai);
                notifyItemChanged(ai);
            }
        }
    }

    public SelectItem[] getSelectedItems() {
        SelectItem[] selectedItems = new SelectItem[mSelectedItemPositions.size()];
        SelectItem[] selectItems = getItems();
        Iterator<Integer> selectedItemPos = mSelectedItemPositions.iterator();
        for(int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = selectItems[selectedItemPos.next()];
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
        if(selectItem.isSelected || mMaxSelection == -1 || mSelectedItemPositions.size() < mMaxSelection) {
            selectItem.isSelected = !selectItem.isSelected;
            if (selectItem.isSelected) {
                mSelectedItemPositions.add(adapterPos);
            } else {
                mSelectedItemPositions.remove(adapterPos);
            }
            if (mChangeListener != null) {
                mChangeListener.onSelectItemChanged(selectItem);
            }
        } else if(mChangeListener != null) {
            mChangeListener.onSelectionLimitReached(mMaxSelection);
        }
        notifyItemChanged(adapterPos);
    }

    public interface OnMultiSelectItemChangeListener {
        void onSelectItemChanged(SelectItem selectItem);
        void onSelectionLimitReached(int limit);
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

        public SelectItemViewHolder(final View itemView) {
            super(itemView);

            tvDisplay = (TextView) itemView.findViewById(R.id.tv_display);
            cbSelected = (CheckBox) itemView.findViewById(R.id.cb_selected);
            cbSelected.setClickable(false);
        }

        @Override
        public void populate(SelectItem entity) {
            tvDisplay.setText(entity.display);
            if(entity.isSelected != cbSelected.isChecked()) {
                cbSelected.performClick();
            }
        }
    }
}
