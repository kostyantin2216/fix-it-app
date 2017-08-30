package com.fixit.ui.adapters.groups;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.Tradesman;
import com.fixit.ui.helpers.OrderedTradesmanInteractionHandler;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.OnItemLongClickListener;
import com.xwray.groupie.ViewHolder;

/**
 * Created by konstantin on 8/8/2017.
 */

public class TradesmanItem extends Item<TradesmanItem.TradesmanViewHolder> implements View.OnClickListener {

    private final Tradesman tradesman;
    private final OrderedTradesmanInteractionHandler handler;

    public TradesmanItem(Tradesman tradesman, OrderedTradesmanInteractionHandler handler) {
        this.tradesman = tradesman;
        this.handler = handler;
    }

    @NonNull
    @Override
    public TradesmanViewHolder createViewHolder(@NonNull View itemView) {
        TradesmanViewHolder viewHolder = new TradesmanViewHolder(itemView);
        viewHolder.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void bind(@NonNull TradesmanViewHolder viewHolder, int position) {
        viewHolder.populate(tradesman);
    }
    
    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public boolean isLongClickable() {
        return false;
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_ordered_tradesman;
    }

    @Override
    public void onClick(View v) {
        handler.onClick(v, tradesman);
    }

    static class TradesmanViewHolder extends ViewHolder {

        private View.OnClickListener onClickListener;

        private final TextView tvCompanyName;
        private final ImageView ivMore;
        private final ImageView ivCall;
        private final ImageView ivMsg;

        public TradesmanViewHolder(@NonNull View rootView) {
            super(rootView);

            tvCompanyName = (TextView) rootView.findViewById(R.id.tv_company_name);
            ivMore = (ImageView) rootView.findViewById(R.id.iv_more);
            ivCall = (ImageView) rootView.findViewById(R.id.iv_call);
            ivMsg = (ImageView) rootView.findViewById(R.id.iv_msg);
        }

        void populate(Tradesman tradesman) {
            tvCompanyName.setText(tradesman.getCompanyName());
        }

        @Override
        public void bind(@NonNull Item item, @Nullable OnItemClickListener onItemClickListener, @Nullable OnItemLongClickListener onItemLongClickListener) {
            super.bind(item, onItemClickListener, onItemLongClickListener);
            
            if(onClickListener != null) {
                ivMore.setOnClickListener(onClickListener);
                ivCall.setOnClickListener(onClickListener);
                ivMsg.setOnClickListener(onClickListener);
            }
        }

        @Override
        public void unbind() {
            super.unbind();

            ivMore.setOnClickListener(null);
            ivCall.setOnClickListener(null);
            ivMsg.setOnClickListener(null);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
    }
}
