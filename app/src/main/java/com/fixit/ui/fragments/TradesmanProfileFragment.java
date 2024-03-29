package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.controllers.TradesmenController;
import com.fixit.data.ReviewData;
import com.fixit.data.Tradesman;
import com.fixit.ui.components.SimpleRatingView;
import com.fixit.ui.components.WorkingDaysView;
import com.fixit.ui.helpers.UITutorials;
import com.fixit.utils.Constants;
import com.fixit.ui.adapters.ReviewRecyclerAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by konstantin on 4/26/2017.
 */

public class TradesmanProfileFragment extends BaseFragment<TradesmenController>
        implements TradesmenController.TradesmanReviewsCallback,
                   View.OnClickListener {

    private TradesmanProfileInteractionListener mListener;

    private Tradesman mTradesman;
    private ReviewData[] mReviewData;

    private ReviewRecyclerAdapter mAdapter;
    private ViewHolder mView;

    private boolean mTradesmanSelectable;

    private static class ViewHolder {

        private enum ListState {
            LOADING,
            EMPTY,
            SHOWING
        }

        final NestedScrollView svProfileScroller;
        final RecyclerView rvReviews;
        final TextView tvNoReviews;
        final ProgressBar pbLoader;
        final Button btnSelect;

        ViewHolder(Context context, View v, Tradesman tradesman, boolean selectable, View.OnClickListener clickListener) {
            svProfileScroller = (NestedScrollView) v.findViewById(R.id.sv_profile_scroller);
            rvReviews = (RecyclerView) v.findViewById(R.id.review_list);
            tvNoReviews = (TextView) v.findViewById(R.id.tv_empty_list);
            pbLoader = (ProgressBar) v.findViewById(R.id.loader);

            rvReviews.setNestedScrollingEnabled(false);
            rvReviews.setLayoutManager(new LinearLayoutManager(context));

            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(tradesman.getCompanyName());

            ImageView ivFeatureImage = (ImageView) v.findViewById(R.id.iv_tradesman_feature);
            String featureImageUrl = tradesman.getFeatureImageUrl();
            Picasso.with(context)
                    .load(featureImageUrl)
                    .placeholder(R.drawable.feature_image)
                    .error(R.drawable.feature_image)
                    .into(ivFeatureImage);

            WorkingDaysView workingDaysView = (WorkingDaysView) v.findViewById(R.id.working_days);
            workingDaysView.setWorkingDays(tradesman.getWorkingDays());

            SimpleRatingView ratingView = (SimpleRatingView) v.findViewById(R.id.tradesman_rating);
            ratingView.setRating(tradesman.getRating());

            changeListState(ListState.LOADING);

            btnSelect = (Button) v.findViewById(R.id.btn_select_tradesman);
            if(!selectable) {
                btnSelect.setVisibility(View.GONE);
                ((CoordinatorLayout.LayoutParams) svProfileScroller.getLayoutParams()).bottomMargin = 0;
            } else {
                btnSelect.setOnClickListener(clickListener);
            }
        }

        void setRecyclerAdapter(RecyclerView.Adapter adapter) {
            if(adapter.getItemCount() > 0) {
                rvReviews.setAdapter(adapter);
                changeListState(ListState.SHOWING);
            } else {
                changeListState(ListState.EMPTY);
            }
        }

        void changeListState(ListState state) {
            switch (state) {
                case LOADING:
                    tvNoReviews.setVisibility(View.GONE);
                    rvReviews.setVisibility(View.GONE);
                    pbLoader.setVisibility(View.VISIBLE);
                    break;
                case EMPTY:
                    rvReviews.setVisibility(View.GONE);
                    pbLoader.setVisibility(View.GONE);
                    tvNoReviews.setVisibility(View.VISIBLE);
                    break;
                case SHOWING:
                    tvNoReviews.setVisibility(View.GONE);
                    pbLoader.setVisibility(View.GONE);
                    rvReviews.setVisibility(View.VISIBLE);
            }
        }
    }

    public static TradesmanProfileFragment newInstance(Tradesman tradesman, boolean selectable) {
        TradesmanProfileFragment fragment = new TradesmanProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_TRADESMAN, tradesman);
        args.putBoolean(Constants.ARG_SELECTABLE, selectable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTradesman = getArguments().getParcelable(Constants.ARG_TRADESMAN);
        mTradesmanSelectable = getArguments().getBoolean(Constants.ARG_SELECTABLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tradesman_profile, container, false);

        setToolbar((Toolbar) v.findViewById(R.id.toolbar), true);

        mView = new ViewHolder(getContext(), v, mTradesman, mTradesmanSelectable, this);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getController().loadReviews(mTradesman.get_id(), this);

        if(mTradesmanSelectable) {
            UITutorials.create(UITutorials.TUTORIAL_SELECT_TRADESMAN, mView.btnSelect, getString(R.string.tutorial_select_tradesman))
                .show(getFragmentManager());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mAdapter == null && mReviewData != null) {
            createAdapter(mReviewData);
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null && v.getId() == R.id.btn_select_tradesman) {
            mListener.onTradesmanSelected(mTradesman);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof TradesmanProfileInteractionListener) {
            mListener = (TradesmanProfileInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onReviewsLoaded(ReviewData[] reviewData) {
        mReviewData = reviewData;
        if(getContext() != null) {
            createAdapter(reviewData);
        }
    }

    private void createAdapter(ReviewData[] reviewData) {
        mAdapter = new ReviewRecyclerAdapter(getContext(), reviewData);
        mView.setRecyclerAdapter(mAdapter);
    }

    public interface TradesmanProfileInteractionListener {
        void onTradesmanSelected(Tradesman tradesman);
    }
}
