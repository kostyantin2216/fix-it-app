package com.fixit.ui.components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fixit.app.R;

/**
 * Created by konstantin on 4/27/2017.
 */

public class SimpleRatingView extends LinearLayout {

    public SimpleRatingView(Context context) {
        this(context, null, 0);
    }

    public SimpleRatingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleRatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        TextView tvRating = new TextView(context);
        tvRating.setGravity(Gravity.CENTER);
        addView(tvRating, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RatingBar ratingBar = new RatingBar(context, null, android.R.attr.ratingBarStyleSmall);
        ratingBar.setMax(5);
        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize(0.5f);

        int ratingStarColor = getResources().getColor(R.color.gold);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(ratingStarColor, PorterDuff.Mode.SRC_ATOP);
        } else {
            Drawable stars = ratingBar.getProgressDrawable();
            stars.setTint(ratingStarColor);
        }

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        addView(ratingBar, lp);

        if(isInEditMode()) {
            setRating(3.5f);
        }
    }

    public void setRating(float rating) {
        TextView tvRating = (TextView) getChildAt(0);
        tvRating.setText(String.format("%.1f", rating));

        RatingBar ratingBar = (RatingBar) getChildAt(1);
        ratingBar.setRating(rating);
    }
}
