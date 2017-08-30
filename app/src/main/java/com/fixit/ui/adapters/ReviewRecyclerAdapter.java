package com.fixit.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.ReviewData;
import com.fixit.utils.DateUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import com.fixit.ui.adapters.CommonRecyclerAdapter.CommonViewHolder;

/**
 * Created by konstantin on 5/7/2017.
 */

public class ReviewRecyclerAdapter extends CommonRecyclerAdapter<ReviewData, ReviewRecyclerAdapter.ReviewViewHolder> {

    public ReviewRecyclerAdapter(Context context, ReviewData[] items) {
        super(context, items);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(v);
        return viewHolder;
    }

    static class ReviewViewHolder extends CommonRecyclerAdapter.CommonViewHolder<ReviewData> implements View.OnClickListener {

        final CircleImageView ivReviewerAvatar;
        final TextView tvReviewerName;
        final TextView tvReviewDate;
        final TextView tvReviewTitle;
        final TextView tvReviewContent;
        final RatingBar rbReviewRating;

        private boolean contentExpanded = false;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            this.ivReviewerAvatar = (CircleImageView) itemView.findViewById(R.id.iv_user_avatar);
            this.tvReviewerName = (TextView) itemView.findViewById(R.id.tv_user_name);
            this.tvReviewDate = (TextView) itemView.findViewById(R.id.tv_review_create_date);
            this.tvReviewTitle = (TextView) itemView.findViewById(R.id.tv_review_title);
            this.tvReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            this.rbReviewRating = (RatingBar) itemView.findViewById(R.id.rb_review_rating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void populate(ReviewData entity) {
            Picasso.with(itemView.getContext()).load(entity.reviewerAvatar).noFade().into(ivReviewerAvatar);
            tvReviewerName.setText(entity.reviewerName);
            tvReviewDate.setText(DateUtils.dateToString(DateUtils.FORMAT_DMY, entity.review.getCreatedAt()));
            tvReviewTitle.setText("\"" + entity.review.getTitle() + "\"");
            tvReviewContent.setText("\"" + entity.review.getContent() + "\"");
            rbReviewRating.setRating(entity.review.getRating());
        }

        @Override
        public void onClick(View v) {
            contentExpanded = !contentExpanded;
            if(contentExpanded) {
                tvReviewContent.setMaxLines(Integer.MAX_VALUE);
            } else {
                tvReviewContent.setMaxLines(3);
            }
        }
    }
}
