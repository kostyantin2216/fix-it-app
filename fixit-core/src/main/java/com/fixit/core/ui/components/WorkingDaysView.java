package com.fixit.core.ui.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixit.core.R;
import com.fixit.core.data.WorkingDay;
import com.fixit.core.data.WorkingHours;
import com.fixit.core.utils.CommonUtils;
import com.fixit.core.utils.DateUtils;
import com.fixit.core.utils.ObjectGenerator;
import com.fixit.core.utils.UIUtils;

import java.util.Date;

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
        isExpanded = true;
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
                    workingDayView.setVisibility(GONE);
                    hidden++;
                } else {
                    workingDayView.setVisibility(VISIBLE);
                }
            }
        }

        isExpanded = !isExpanded;

        if(hidden == childCount) {
            addView(tvClosedToday);
        }
    }

}
