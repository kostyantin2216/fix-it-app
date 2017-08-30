package com.fixit.ui.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.WorkingDay;
import com.fixit.utils.DateUtils;
import com.fixit.utils.ObjectGenerator;
import com.fixit.utils.UIUtils;

/**
 * Created by konstantin on 4/27/2017.
 */

public class WorkingDaysView extends LinearLayout implements View.OnClickListener {

    private final TextView tvClosedToday;
    private final int today = DateUtils.getCurrentDayOfWeek();

    private boolean isExpanded = true;

    public WorkingDaysView(Context context) {
        this(context, null, 0);
    }

    public WorkingDaysView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkingDaysView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        final int padding = UIUtils.dpToPx(context, 8);
        tvClosedToday = new TextView(context);
        tvClosedToday.setText(R.string.closed_today);
        tvClosedToday.setGravity(Gravity.CENTER);
        tvClosedToday.setPadding(padding, padding, padding, padding);
        tvClosedToday.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if(isInEditMode()) {
            setWorkingDays(ObjectGenerator.createWorkingDays());
        } else {
            setOnClickListener(this);
        }
    }

    public void setWorkingDays(WorkingDay[] workingDays) {
        removeAllViews();
        final Context context = getContext();
        final int padding = UIUtils.dpToPx(context, 4);
        for(int i = 0; i < workingDays.length; i++) {
            WorkingDay workingDay = workingDays[i];
            if(workingDay != null) {
                WorkingDayView dayView = new WorkingDayView(context);
                dayView.setWorkingDay(workingDay);
                dayView.setPadding(
                        dayView.getPaddingLeft(),
                        dayView.getPaddingTop() + (i == 0 ? padding : 0),
                        dayView.getPaddingRight(),
                        dayView.getPaddingBottom() + padding
                );

                addView(dayView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        isExpanded = false;
        toggleWorkingDaysVisibility();
    }

    @Override
    public void onClick(View v) {
        toggleWorkingDaysVisibility();
    }

    public void toggleWorkingDaysVisibility() {
        removeView(tvClosedToday);
        final int childCount = getChildCount();

        int hidden = 0;
        for(int i = 0; i < childCount; i++) {
            WorkingDayView workingDayView = (WorkingDayView) getChildAt(i);
            if(workingDayView.getWorkingDay().getDayOfWeek() != today) {
                if(isExpanded) {
                    //workingDayView.setVisibility(GONE);
                    if(workingDayView.getVisibility() == VISIBLE) {
                        UIUtils.collapse(workingDayView, 3);
                    }
                    hidden++;
                } else {
                    //workingDayView.setVisibility(VISIBLE);
                    if(workingDayView.getVisibility() != VISIBLE) {
                        UIUtils.expand(workingDayView, 3);
                    }
                }
            }
        }

        isExpanded = !isExpanded;

        if(hidden == childCount) {
            addView(tvClosedToday);
        }
    }

}
