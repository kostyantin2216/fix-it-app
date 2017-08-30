package com.fixit.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.WorkingDay;
import com.fixit.data.WorkingHours;
import com.fixit.utils.CommonUtils;

/**
 * Created by konstantin on 4/27/2017.
 */
public class WorkingDayView extends LinearLayout {

    private WorkingDay mWorkingDay;

    public WorkingDayView(Context context) {
        this(context, null, 0);
    }

    public WorkingDayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkingDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);

        TextView tvDay = new TextView(context);
        tvDay.setId(View.generateViewId());
        tvDay.setGravity(Gravity.CENTER_VERTICAL);

        LayoutParams dayLp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        dayLp.weight = 0.4f;

        addView(tvDay, dayLp);

        LinearLayout hoursContainer = new LinearLayout(context);
        hoursContainer.setOrientation(VERTICAL);

        LayoutParams hoursLp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        hoursLp.weight = 0.6f;

        addView(hoursContainer, hoursLp);
    }

    public void setWorkingDay(WorkingDay workingDay) {
        mWorkingDay = workingDay;
        invalidateWorkingDays();
    }

    private void invalidateWorkingDays() {
        setDayOfWeek((TextView) getChildAt(0));

        ViewGroup hoursContainer = (ViewGroup) getChildAt(1);
        hoursContainer.removeAllViews();

        Context context = getContext();
        for(WorkingHours workingHours : mWorkingDay.getHours()) {
            TextView tvHours = new TextView(context);
            tvHours.setGravity(Gravity.CENTER);
            setHours(tvHours, workingHours);
            hoursContainer.addView(tvHours, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void setDayOfWeek(TextView tv) {
        String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        String display = daysOfWeek[mWorkingDay.getDayOfWeek() - 1] + ":";
        tv.setText(display);
    }

    private void setHours(TextView tv, WorkingHours workingHours) {
        String open = CommonUtils.hundredthsToTimeDisplay(workingHours.getOpen());
        String close = CommonUtils.hundredthsToTimeDisplay(workingHours.getClose());
        String display = open + " - " + close;
        tv.setText(display);
    }

    public WorkingDay getWorkingDay() {
        return mWorkingDay;
    }
}
