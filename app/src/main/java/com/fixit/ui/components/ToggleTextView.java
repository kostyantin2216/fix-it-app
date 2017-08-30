package com.fixit.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.fixit.app.R;

/**
 * Created by Kostyantin on 5/27/2017.
 */

public class ToggleTextView extends AppCompatTextView
    implements View.OnClickListener {

    private final static int DEFAULT_ON_TEXT_COLOR = Color.GREEN;
    private final static int DEFAULT_OFF_TEXT_COLOR = Color.RED;

    private ToggleTextViewListener mListener;

    private String offText;
    private String onText;

    private int offTextColor;
    private int onTextColor;

    private boolean on;

    public ToggleTextView(Context context) {
        this(context, null);
    }

    public ToggleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        if(attrs == null) {
            offText = context.getString(R.string.off);
            onText = context.getString(R.string.on);

            offTextColor = DEFAULT_OFF_TEXT_COLOR;
            onTextColor = DEFAULT_ON_TEXT_COLOR;

            on = false;
        } else {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToggleTextView);

            offText = ta.getString(R.styleable.ToggleTextView_offText);
            onText = ta.getString(R.styleable.ToggleTextView_onText);

            offTextColor = ta.getColor(R.styleable.ToggleTextView_offTextColor, DEFAULT_OFF_TEXT_COLOR);
            onTextColor = ta.getColor(R.styleable.ToggleTextView_onTextColor, DEFAULT_ON_TEXT_COLOR);

            on = ta.getBoolean(R.styleable.ToggleTextView_on, false);

            ta.recycle();
        }

        invalidateState();

        if(!isInEditMode()) {
            setOnClickListener(this);
        }
    }

    public void setToggleListener(ToggleTextViewListener listener) {
        mListener = listener;
    }

    public void setOffText(String offText) {
        this.offText = offText;
    }

    public void setOnText(String onText) {
        this.onText = onText;
    }

    public void setOffTextColor(int offTextColor) {
        this.offTextColor = offTextColor;
    }

    public void setOnTextColor(int onTextColor) {
        this.onTextColor = onTextColor;
    }

    public boolean isOn() {
        return on;
    }

    public void invalidateState() {
        setText(on ? onText : offText);
        setTextColor(on? onTextColor : offTextColor);
    }

    public void toggle() {
        onClick(this);
    }

    @Override
    public void onClick(View v) {
        on = !on;
        invalidateState();
        if (mListener != null) {
            mListener.onToggle(on);
        }
    }

    public interface ToggleTextViewListener {
        void onToggle(boolean on);
    }
}
