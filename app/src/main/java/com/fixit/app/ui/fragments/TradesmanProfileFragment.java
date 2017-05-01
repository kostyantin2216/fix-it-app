package com.fixit.app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fixit.app.R;
import com.fixit.core.controllers.TradesmenController;
import com.fixit.core.data.Tradesman;
import com.fixit.core.ui.components.SimpleRatingView;
import com.fixit.core.ui.components.WorkingDaysView;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.utils.Constants;

/**
 * Created by konstantin on 4/26/2017.
 */

public class TradesmanProfileFragment extends BaseFragment<TradesmenController> {

    private Tradesman mTradesman;



    public static TradesmanProfileFragment newInstance(Tradesman tradesman) {
        TradesmanProfileFragment fragment = new TradesmanProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_TRADESMAN, tradesman);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTradesman = getArguments().getParcelable(Constants.ARG_TRADESMAN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tradesman_profile, container, false);

        setToolbar((Toolbar) v.findViewById(R.id.toolbar), true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mTradesman.getCompanyName());

        WorkingDaysView workingDaysView = (WorkingDaysView) v.findViewById(R.id.working_days);
        workingDaysView.setWorkingDays(mTradesman.getWorkingDays());

        SimpleRatingView ratingView = (SimpleRatingView) v.findViewById(R.id.tradesman_rating);
        ratingView.setRating(mTradesman.getRating());

        return v;
    }
}
