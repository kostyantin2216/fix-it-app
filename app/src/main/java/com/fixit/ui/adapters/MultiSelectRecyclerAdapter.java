package com.fixit.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.ui.adapters.MultiSelectRecyclerAdapter.SelectItem;
import com.fixit.ui.adapters.MultiSelectRecyclerAdapter.SelectItemViewHolder;
import com.fixit.ui.adapters.animations.RecyclerItemAnimation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fixit.ui.adapters.CommonRecyclerAdapter.CommonViewHolder;
/**
 * Created by Kostyantin on 6/1/2017.
 */

public class MultiSelectRecyclerAdapter extends CommonRecyclerAdapter<SelectItem, SelectItemViewHolder> implements View.OnClickListener {

    private OnMultiSelectItemChangeListener mChangeListener;
    private Set<Integer> mSelectedItemPositions = new HashSet<>();

    private final int mMaxSelection;

    public MultiSelectRecyclerAdapter(RecyclerItemAnimation itemAnimation, SelectItem[] items, int maxSelection) {
        super(itemAnimation, items, null, SelectItem.COMPARATOR);
        this.mMaxSelection = maxSelection;
    }

    public MultiSelectRecyclerAdapter(Context context, SelectItem[] items, int maxSelection) {
        super(context, items, null, SelectItem.COMPARATOR);
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
        List<SelectItem> adapterItems = getItems();

        mSelectedItemPositions.clear();
        for(int ai = 0; ai < adapterItems.size(); ai++) {
            SelectItem adapterItem = adapterItems.get(ai);
            boolean selected = false;

            for(int si = 0; si < selectedItems.length; si++) {
                if(selectedItems[si] != null && selectedItems[si].code == adapterItem.code) {
                    selectedItems[si] = null;
                    selected = true;
                    break;
                }
            }

            if(!selected) {
                adapterItem.isSelected = false;
            } else {
                adapterItem.isSelected = true;
                mSelectedItemPositions.add(ai);

                int adapterPosition = getAdapterPosition(adapterItem);
                if(adapterPosition > -1) {
                    notifyItemChanged(adapterPosition);
                }
            }
        }
    }

    public SelectItem[] getSelectedItems() {
        SelectItem[] selectedItems = new SelectItem[mSelectedItemPositions.size()];
        List<SelectItem> selectItems = getItems();
        Iterator<Integer> selectedItemPos = mSelectedItemPositions.iterator();
        for(int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = selectItems.get(selectedItemPos.next());
        }
        Arrays.sort(selectedItems, SelectItem.COMPARATOR);
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
        SelectItem adapterItem = getAdapterItem(adapterPos);
        if(adapterItem.isSelected || mMaxSelection == -1 || mSelectedItemPositions.size() < mMaxSelection) {
            adapterItem.isSelected = !adapterItem.isSelected;
            int itemPos = getItemPosition(adapterItem);
            if (adapterItem.isSelected) {
                mSelectedItemPositions.add(itemPos);
            } else {
                mSelectedItemPositions.remove(itemPos);
            }
            if (mChangeListener != null) {
                mChangeListener.onSelectItemChanged(adapterItem);
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

        private final static Comparator<SelectItem> COMPARATOR = new Comparator<SelectItem>() {
            @Override
            public int compare(SelectItem o1, SelectItem o2) {
                return o1.display.compareTo(o2.display);
            }
        };
    }

    static class SelectItemViewHolder extends CommonViewHolder<SelectItem> {

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
