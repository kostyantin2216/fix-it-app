package com.fixit.core.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fixit.core.ui.adapters.animations.RecyclerItemAnimation;
import com.fixit.core.ui.adapters.animations.RecyclerItemSlideInAnimation;

import java.util.List;

/**
 * Created by konstantin on 3/9/2017.
 */

public abstract class CommonRecyclerAdapter<E, VH extends CommonRecyclerAdapter.CommonViewHolder<E>>
        extends AnimatedRecyclerAdapter<VH> {

    private CommonRecyclerViewInteractionsListener<E> listener;
    private List<E> items;

    public CommonRecyclerAdapter(RecyclerItemAnimation itemAnimation, List<E> items, CommonRecyclerViewInteractionsListener<E> listener) {
        super(itemAnimation);
        this.items = items;
        this.listener = listener;
    }

    public CommonRecyclerAdapter(Context context, List<E> items, CommonRecyclerViewInteractionsListener<E> listener) {
        this(new RecyclerItemSlideInAnimation(context, RecyclerItemSlideInAnimation.Direction.getRandom(), true), items, listener);
    }

    public CommonRecyclerAdapter(Context context, List<E> items) {
        this(context, items, null);
    }

    public void setInteractionListener(CommonRecyclerViewInteractionsListener<E> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<E> getItems() {
        return items;
    }

    public E getItem(int position) {
        return items.get(position);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.populate(items.get(position));
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
                        listener.onItemClick(items.get(position));
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

    public interface CommonRecyclerViewInteractionsListener<E> {
        void onItemClick(E item);
    }

}
