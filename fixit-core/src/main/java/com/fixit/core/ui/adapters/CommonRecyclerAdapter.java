package com.fixit.core.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fixit.core.ui.adapters.animations.RecyclerItemAnimation;
import com.fixit.core.ui.adapters.animations.RecyclerItemSlideInAnimation;

/**
 * Created by konstantin on 3/9/2017.
 */

public abstract class CommonRecyclerAdapter<E, VH extends CommonRecyclerAdapter.CommonViewHolder<E>>
        extends AnimatedRecyclerAdapter<VH> {

    private CommonRecyclerViewInteractionListener<E> listener;
    private E[] items;

    public CommonRecyclerAdapter(RecyclerItemAnimation itemAnimation, E[] items, CommonRecyclerViewInteractionListener<E> listener) {
        super(itemAnimation);
        this.items = items;
        this.listener = listener;
    }

    public CommonRecyclerAdapter(RecyclerItemAnimation itemAnimation, E[] items) {
        this(itemAnimation, items, null);
    }

    public CommonRecyclerAdapter(Context context, E[] items, CommonRecyclerViewInteractionListener<E> listener) {
        this(new RecyclerItemSlideInAnimation(context, RecyclerItemSlideInAnimation.Direction.getRandom(), true),
                items, listener);
    }

    public CommonRecyclerAdapter(Context context, E[] items) {
        this(context, items, null);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public E[] getItems() {
        return items;
    }

    public E getItem(int position) {
        return items[position];
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.populate(items[position]);
    }

    protected void attachItemClickListener(View v, VH vh) {
        v.setTag(vh);
        if(listener != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                @SuppressWarnings("unchecked")
                public void onClick(View v) {
                    VH vh = (VH) v.getTag();
                    int position = vh.getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items[position]);
                    }
                }
            });
        }
    }

    public static abstract class CommonViewHolder<E> extends RecyclerView.ViewHolder {

        public CommonViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void populate(E entity);
    }

    public interface CommonRecyclerViewInteractionListener<E> {
        void onItemClick(E item);
    }

}
