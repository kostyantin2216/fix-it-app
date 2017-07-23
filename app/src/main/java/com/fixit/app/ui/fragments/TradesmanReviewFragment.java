package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.core.controllers.ReviewController;
import com.fixit.core.data.Review;
import com.fixit.core.data.Tradesman;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.GlobalPreferences;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by konstantin on 7/23/2017.
 */

public class TradesmanReviewFragment extends BaseFragment<ReviewController> implements View.OnClickListener, ReviewController.LoadReviewsCallback {

    private Tradesman tradesman;
    private ViewHolder mView;

    private TradesmanReviewListener mListener;

    private boolean newReview = true;

    public static TradesmanReviewFragment newInstance(Tradesman tradesman) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_TRADESMAN, tradesman);

        TradesmanReviewFragment fragment = new TradesmanReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tradesman = getArguments().getParcelable(Constants.ARG_TRADESMAN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tradesman_review, container, false);

        mView = new ViewHolder(v, this);
        mView.populate(tradesman);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoader(getString(R.string.checking_review_history));
        getController().loadReviewForTradesmanByUser(tradesman, this);
    }

    @Override
    public void onReviewsLoaded(List<Review> reviews) {
        if(reviews != null && !reviews.isEmpty()) {
            mView.showExistingReview(reviews.get(0));
            newReview = false;
        }
        hideLoader();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit) {
            Review review = mView.fieldsToReview();
            review.setTradesmanId(tradesman.get_id());
            review.setUserId(GlobalPreferences.getUserId(getContext()));
            review.setOnDisplay(true);
            review.setCreatedAt(new Date());

            if(mListener != null) {
                mListener.onTradesmanReviewed(newReview, review);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof TradesmanReviewListener) {
            mListener = (TradesmanReviewListener) context;
        } else {
            throw new IllegalStateException(context.getClass().getName() + " must implement "
                        + TradesmanReviewListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public interface TradesmanReviewListener {
        void onTradesmanReviewed(boolean isNewReview, Review review);
    }

    public static class ViewHolder implements RatingBar.OnRatingBarChangeListener {
        final TextView tvStatus;
        final ImageView ivTradesmanLogo;
        final TextView tvCompanyName;
        final RatingBar rbRating;
        final EditText etReviewTitle;
        final EditText etReview;
        final Button btnSubmit;

        ViewHolder(View v, View.OnClickListener submitClickListener) {
            tvStatus = (TextView) v.findViewById(R.id.tv_review_status);
            ivTradesmanLogo = (ImageView) v.findViewById(R.id.iv_tradesman_logo);
            tvCompanyName = (TextView) v.findViewById(R.id.tv_company_name);
            rbRating = (RatingBar) v.findViewById(R.id.rb_rating);
            etReviewTitle = (EditText) v.findViewById(R.id.et_review_title);
            etReview = (EditText) v.findViewById(R.id.et_review_content);
            btnSubmit = (Button) v.findViewById(R.id.btn_submit);

            btnSubmit.setOnClickListener(submitClickListener);
            rbRating.setOnRatingBarChangeListener(this);
        }

        void populate(Tradesman tradesman) {
            Picasso.with(ivTradesmanLogo.getContext()).load(tradesman.getLogoUrl()).into(ivTradesmanLogo);
            tvCompanyName.setText(tradesman.getCompanyName());
        }

        void showExistingReview(Review review) {
            tvStatus.setText(tvStatus.getResources().getString(R.string.update_existing_review));
            rbRating.setRating(review.getRating());
            etReviewTitle.setText(review.getTitle());
            etReview.setText(review.getContent());
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            btnSubmit.setVisibility(rating > 0.0f ? View.VISIBLE : View.INVISIBLE);
        }

        Review fieldsToReview() {
            Review review = new Review();
            review.setRating(rbRating.getRating());
            review.setTitle(etReviewTitle.getText().toString());
            review.setContent(etReview.getText().toString());
            return review;
        }
    }

}
