package com.fixit.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.TradesmanWrapper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by konstantin on 3/30/2017.
 */

public class TradesmenAdapter extends RecyclerView.Adapter<TradesmenAdapter.TradesmanViewHolder> implements View.OnClickListener {

    private final TradesmenAdapterCallback mCallback;

    private final List<TradesmanWrapper> mTradesmen = new ArrayList<>();
    private final Set<Integer> mSelectedTradesmenPositions;

    private final int mMaxTradesmenSelection;
    private final float mRatingBarScale;

    public TradesmenAdapter(@Nullable TradesmenAdapterCallback callback) {
        this(0, callback, -1);
    }

    public TradesmenAdapter(@Nullable TradesmenAdapterCallback callback, float ratingBarScale) {
        this(0, callback, ratingBarScale);
    }

    public TradesmenAdapter(int maxTradesmenSelection, @Nullable TradesmenAdapterCallback callback) {
        this(maxTradesmenSelection, callback, -1);
    }

    public TradesmenAdapter(int maxTradesmenSelection, @Nullable TradesmenAdapterCallback callback, float ratingBarScale) {
        mCallback = callback;
        mMaxTradesmenSelection = maxTradesmenSelection;
        mSelectedTradesmenPositions = new HashSet<>(maxTradesmenSelection);
        mRatingBarScale = ratingBarScale;
    }

    @Override
    public TradesmanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tradesman, parent, false);
        TradesmanViewHolder vh = new TradesmanViewHolder(v, mRatingBarScale);

        if(mCallback != null) {
            v.setTag(vh);
            v.setOnClickListener(this);
        }

        return vh;
    }

    @Override
    public void onClick(View v) {
        assert mCallback != null;

        TradesmanViewHolder vh = (TradesmanViewHolder) v.getTag();
        int position = vh.getAdapterPosition();

        if(position != RecyclerView.NO_POSITION) {
            TradesmanWrapper tradesman = mTradesmen.get(position);
            if(vh.isSelected) {
                tradesman.setSelected(false);
                mSelectedTradesmenPositions.remove(position);
                notifyItemChanged(position);
                mCallback.onTradesmanUnselected(mSelectedTradesmenPositions.size() > 0);
            } else {
                mCallback.onTradesmanClick(position, tradesman);
            }
        }
    }

    @Override
    public void onBindViewHolder(TradesmanViewHolder holder, int position) {
        holder.populate(mTradesmen.get(position));
    }

    @Override
    public int getItemCount() {
        return mTradesmen.size();
    }

    public int getSelectedItemCount() {
        return mSelectedTradesmenPositions.size();
    }

    public void setTradesmen(List<TradesmanWrapper> tradesmen) {
        int oldCount = mTradesmen.size();
        mTradesmen.clear();
        mSelectedTradesmenPositions.clear();
        notifyItemRangeRemoved(0, oldCount);
        for (int i = 0, count = tradesmen.size(); i < count; i++) {
            TradesmanWrapper tradesman = tradesmen.get(i);
            if(tradesman.isSelected()) {
                if(mSelectedTradesmenPositions.size() < mMaxTradesmenSelection) {
                    mSelectedTradesmenPositions.add(i);
                } else {
                    tradesman.setSelected(false);
                }
            }
            mTradesmen.add(tradesman);
            notifyItemInserted(i);
        }
    }

    public List<TradesmanWrapper> getSelectedTradesmen() {
        List<TradesmanWrapper> tradesmen = new ArrayList<>();
        for(int position : mSelectedTradesmenPositions) {
            tradesmen.add(mTradesmen.get(position));
        }
        return tradesmen;
    }

    public boolean selectTradesman(int position) {
        if(mSelectedTradesmenPositions.size() < mMaxTradesmenSelection) {
            TradesmanWrapper tradesman = mTradesmen.get(position);
            tradesman.setSelected(true);
            mSelectedTradesmenPositions.add(position);
            notifyItemChanged(position);
            return true;
        }
        return false;
    }

    public interface TradesmenAdapterCallback {
        void notifyUser(String msg);
        void onTradesmanClick(int adapterPosition, TradesmanWrapper tradesman);
        void onTradesmanUnselected(boolean hasMoreSelections);
    }

    static class TradesmanViewHolder extends RecyclerView.ViewHolder {

        final ImageView ivLogo;
        final TextView tvCompanyName;
        final RatingBar rbRating;
        final TextView tvReviewCount;

        boolean isSelected = false;

        TradesmanViewHolder(View itemView, float ratingBarScale) {
            super(itemView);

            this.ivLogo = (ImageView) itemView.findViewById(R.id.iv_tradesman_logo);
            this.tvCompanyName = (TextView) itemView.findViewById(R.id.tv_company_name);
            this.rbRating = (RatingBar) itemView.findViewById(R.id.rb_tradesman_rating);
            this.tvReviewCount = (TextView) itemView.findViewById(R.id.tv_tradesman_review_count);

            if(ratingBarScale >= 0) {
                this.rbRating.setScaleX(ratingBarScale);
                this.rbRating.setScaleY(ratingBarScale);
            }
        }

        void populate(TradesmanWrapper dateHolder) {
            String logoUrl = dateHolder.tradesman.getLogoUrl();
            if(!TextUtils.isEmpty(logoUrl)) {
                Picasso.with(itemView.getContext()).load(logoUrl).into(ivLogo);
            }

            tvCompanyName.setText(dateHolder.tradesman.getCompanyName());
            rbRating.setRating(dateHolder.tradesman.getRating());
            tvReviewCount.setText(itemView.getContext().getString(R.string.format_review_count, dateHolder.reviewCount));

            isSelected = dateHolder.isSelected();
            if(isSelected) {
                itemView.setBackgroundResource(R.drawable.list_item_bg_transparent_green);
            } else {
                itemView.setBackgroundResource(R.drawable.list_item_bg_transparent_white);
            }
        }
    }

}
