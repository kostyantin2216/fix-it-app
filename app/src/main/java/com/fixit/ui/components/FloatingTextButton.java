package com.fixit.ui.components;

/**
 * Created by Kostyantin on 6/22/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.utils.UIUtils;

public class FloatingTextButton extends CardView {

    private ImageView iconView;
    private TextView titleView;

    private String title;
    private int titleColor;
    private Drawable icon;
    private int background;

    public FloatingTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout(context);
        initAttributes(attrs);
        initView();
    }

    private void inflateLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_floating_text_button, this, true);

        iconView = (ImageView) findViewById(R.id.layout_button_image);
        titleView = (TextView) findViewById(R.id.layout_button_text);
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray styleable = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.FloatingTextButton,
                0,
                0
        );

        title = styleable.getString(R.styleable.FloatingTextButton_buttonText);
        titleColor = styleable.getColor(R.styleable.FloatingTextButton_buttonTextColor, Color.BLACK);
        icon = styleable.getDrawable(R.styleable.FloatingTextButton_buttonIcon);
        background = styleable.getColor(R.styleable.FloatingTextButton_buttonColor, Color.BLUE);

        styleable.recycle();
    }

    private void initView() {
        setTitle(title);
        setTitleColor(titleColor);
        setIconDrawable(icon);
        setCardBackgroundColor(background);

        setContentPadding(
                getHorizontalPaddingValue(16),
                getVerticalPaddingValue(8),
                getHorizontalPaddingValue(16),
                getVerticalPaddingValue(8)
        );
        initViewRadius();
    }

    private void initViewRadius() {
        post(new Runnable() {
            @Override
            public void run() {
                setRadius(getHeight() / 2);
            }
        });
    }

    public void setTitle(String newTitle) {
        title = newTitle;

        if (newTitle == null || newTitle.isEmpty()) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
        }

        titleView.setText(newTitle);
    }

    public void setTitleColor(@ColorInt int color) {
        titleView.setTextColor(color);
    }

    public void setIconDrawable(Drawable drawable) {
        if (drawable != null) {
            iconView.setVisibility(VISIBLE);
            iconView.setImageDrawable(drawable);
        } else {
            iconView.setVisibility(GONE);
        }
    }

    private int getVerticalPaddingValue(int dp) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return UIUtils.dpToPx(getContext(), dp) / 4;
        } else {
            return UIUtils.dpToPx(getContext(), dp);
        }
    }

    private int getHorizontalPaddingValue(int dp) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return UIUtils.dpToPx(getContext(), dp) / 2;
        } else {
            return UIUtils.dpToPx(getContext(), dp);
        }
    }
}
