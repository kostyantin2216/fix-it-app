package com.fixit.app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.core.config.AppConfig;
import com.fixit.core.data.Tradesman;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by konstantin on 3/30/2017.
 */

public class TradesmenAdapter extends RecyclerView.Adapter<TradesmenAdapter.TradesmanViewHolder> implements View.OnClickListener {

    private final TradesmanAdapterCallback mCallback;

    private final List<TradesmanHolder> mTradesmen = new ArrayList<>();
    private final Set<Integer> mSelectedTradesmenPositions;

    private final int mMaxTradesmenSelection;

    public TradesmenAdapter(Context context, TradesmanAdapterCallback callback) {
        mCallback = callback;
        mMaxTradesmenSelection = AppConfig.getInt(context, AppConfig.KEY_MAX_TRADESMEN_SELECTION, 3);
        mSelectedTradesmenPositions = new HashSet<>(mMaxTradesmenSelection);
    }

    @Override
    public TradesmanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tradesman, parent, false);
        TradesmanViewHolder vh = new TradesmanViewHolder(v);

        vh.itemView.setTag(vh);
        vh.containerLogo.setTag(vh);

        vh.itemView.setOnClickListener(this);
        vh.containerLogo.setOnClickListener(this);

        return vh;
    }

    @Override
    public void onBindViewHolder(TradesmanViewHolder holder, int position) {
        holder.populate(mTradesmen.get(position));
    }

    @Override
    public int getItemCount() {
        return mTradesmen.size();
    }

    public void setTradesmen(List<TradesmanHolder> tradesmen) {
        int oldCount = mTradesmen.size();
        mTradesmen.clear();
        notifyItemRangeRemoved(0, oldCount);
        for (int i = 0, count = tradesmen.size(); i < count; i++) {
            mTradesmen.add(tradesmen.get(i));
        }
        notifyItemRangeInserted(0, tradesmen.size());
    }

    @Override
    public void onClick(View v) {
        TradesmanViewHolder vh = (TradesmanViewHolder) v.getTag();
        int position = vh.getAdapterPosition();

        if(position != RecyclerView.NO_POSITION) {
            if (v.getId() == R.id.container_logo) {
                if(vh.isSelected) {
                    vh.deselect();
                    mSelectedTradesmenPositions.remove(position);
                } else if(mSelectedTradesmenPositions.size() < mMaxTradesmenSelection) {
                    vh.select();
                    mSelectedTradesmenPositions.add(position);
                } else {
                    mCallback.notifyUser(v.getContext().getString(R.string.tradesman_selection_limit_passed, mMaxTradesmenSelection));
                }
            } else {
                mCallback.onTradesmanSelected(mTradesmen.get(position).tradesman);
            }
        }
    }

    public List<Tradesman> getSelectedTradesmen() {
        List<Tradesman> tradesmen = new ArrayList<>();
        for(int position : mSelectedTradesmenPositions) {
            tradesmen.add(mTradesmen.get(position).tradesman);
        }
        return tradesmen;
    }

    public interface TradesmanAdapterCallback {
        void notifyUser(String msg);
        void onTradesmanSelected(Tradesman tradesman);
    }

    static class TradesmanViewHolder extends RecyclerView.ViewHolder {

        final ViewGroup containerLogo;
        final ImageView ivLogo;
        final TextView tvCompanyName;
        final RatingBar rbRating;
        final TextView tvReviewCount;

        boolean isSelected = false;

        public TradesmanViewHolder(View itemView) {
            super(itemView);

            this.containerLogo = (ViewGroup) itemView.findViewById(R.id.container_logo);
            this.ivLogo = (ImageView) itemView.findViewById(R.id.iv_tradesman_logo);
            this.tvCompanyName = (TextView) itemView.findViewById(R.id.tv_company_name);
            this.rbRating = (RatingBar) itemView.findViewById(R.id.rb_tradesman_rating);
            this.tvReviewCount = (TextView) itemView.findViewById(R.id.tv_tradesman_review_count);
        }

        public void populate(TradesmanHolder dateHolder) {
            String logoUrl = dateHolder.tradesman.getLogoUrl();
            if(!TextUtils.isEmpty(logoUrl)) {
                Picasso.with(itemView.getContext()).load(logoUrl).into(ivLogo);
            }

            tvCompanyName.setText(dateHolder.tradesman.getCompanyName());
            rbRating.setRating(dateHolder.tradesman.getRating());
            tvReviewCount.setText(itemView.getContext().getString(R.string.format_review_count, dateHolder.reviewCount));

            if(isSelected) {
                itemView.setBackgroundResource(R.drawable.list_item_bg_transparent_white_green_border);
            } else {
                itemView.setBackgroundResource(R.drawable.list_item_bg_transparent_white);
            }
        }

        public void select() {
            isSelected = true;
            itemView.setBackgroundResource(R.drawable.list_item_bg_transparent_white_green_border);
        }

        public void deselect() {
            isSelected = false;
            itemView.setBackgroundResource(R.drawable.list_item_bg_transparent_white);
        }

    }

    public static class TradesmanHolder {
        public final Tradesman tradesman;
        public final int reviewCount;

        public TradesmanHolder(Tradesman tradesman, int reviewCount) {
            this.tradesman = tradesman;
            this.reviewCount = reviewCount;
        }
    }


}
