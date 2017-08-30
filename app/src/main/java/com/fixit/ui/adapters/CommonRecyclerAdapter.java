package com.fixit.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fixit.ui.adapters.animations.RecyclerItemAnimation;
import com.fixit.ui.adapters.animations.RecyclerItemSlideInAnimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by konstantin on 3/9/2017.
 */

public abstract class CommonRecyclerAdapter<E, VH extends CommonRecyclerAdapter.CommonViewHolder<E>>
        extends AnimatedRecyclerAdapter<VH> {

    private CommonRecyclerViewInteractionListener<E> listener;
    private final List<E> items;
    private List<E> adapterItems;
    private final Comparator<E> comparator;

    private boolean notifyOnClick = false;

    public CommonRecyclerAdapter(RecyclerItemAnimation itemAnimation, E[] items, CommonRecyclerViewInteractionListener<E> listener, Comparator<E> comparator) {
        super(itemAnimation);
        this.items = Arrays.asList(items);
        this.adapterItems = new ArrayList<>();
        for(E item : items) {
            this.adapterItems.add(item);
        }
        this.listener = listener;
        this.comparator = comparator;
        if(this.comparator != null) {
            Collections.sort(adapterItems, comparator);
        }
    }

    public CommonRecyclerAdapter(RecyclerItemAnimation itemAnimation, E[] items, CommonRecyclerViewInteractionListener<E> listener) {
        this(itemAnimation, items, listener, null);
    }

    public CommonRecyclerAdapter(RecyclerItemAnimation itemAnimation, E[] items) {
        this(itemAnimation, items, null, null);
    }

    public CommonRecyclerAdapter(Context context, E[] items, CommonRecyclerViewInteractionListener<E> listener, Comparator<E> comparator) {
        this(new RecyclerItemSlideInAnimation(context, RecyclerItemSlideInAnimation.Direction.getRandom(), true),
                items, listener, comparator);
    }

    public CommonRecyclerAdapter(Context context, E[] items, CommonRecyclerViewInteractionListener<E> listener) {
        this(new RecyclerItemSlideInAnimation(context, RecyclerItemSlideInAnimation.Direction.getRandom(), true),
                items, listener, null);
    }

    public CommonRecyclerAdapter(Context context, E[] items) {
        this(context, items, null);
    }

    void setNotifyOnClick(boolean notifyOnClick) {
        this.notifyOnClick = notifyOnClick;
    }

    @Override
    public int getItemCount() {
        return adapterItems.size();
    }

    public List<E> getItems() {
        return items;
    }

    public E getItem(int position) {
        return adapterItems.get(position);
    }

    public int getItemPosition(E item) {
        return items.indexOf(item);
    }

    public List<E> getAdapterItems() {
        return adapterItems;
    }

    public E getAdapterItem(int position) {
        return adapterItems.get(position);
    }

    public int getAdapterPosition(E item) {
        return adapterItems.indexOf(item);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.populate(getAdapterItem(position));
    }

    protected void attachItemClickListener(View v, VH vh) {
        v.setTag(vh);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onClick(View v) {
                VH vh = (VH) v.getTag();
                int position = vh.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    E item = getAdapterItem(position);
                    if (listener != null) {
                        listener.onItemClick(vh, item);
                    }
                    if(notifyOnClick) {
                        onItemClick(vh, item);
                    }
                }
            }
        });

    }

    void onItemClick(RecyclerView.ViewHolder vh, E item) {
        // override if needed
    }

    public <V> void filter(AdapterFilterer<E> filterer) {
        adapterItems.clear();
        for(E item : items) {
            if(!filterer.filter(item)) {
                adapterItems.add(item);
            }
        }

        if(this.comparator != null) {
            Collections.sort(adapterItems, comparator);
        }

        notifyDataSetChanged();
    }

    public static abstract class CommonViewHolder<E> extends RecyclerView.ViewHolder {

        public CommonViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void populate(E entity);
    }

    public interface CommonRecyclerViewInteractionListener<E> {
        void onItemClick(RecyclerView.ViewHolder vh, E item);
    }

    public interface AdapterFilterer<E> {
        /**
         * @return true to hide item, false to make it visible
         */
        boolean filter(E data);
    }

}
