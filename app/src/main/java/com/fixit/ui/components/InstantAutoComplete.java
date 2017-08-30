package com.fixit.ui.components;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by konstantin on 3/30/2017.
 */
public class InstantAutoComplete extends CancelableAutoCompleteTextView implements View.OnClickListener {

    public InstantAutoComplete(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public InstantAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public InstantAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (previouslyFocusedRect != null && focused && getFilter() != null) {
            performFiltering(getText(), 0);
            showDropDown();
        }
    }

    @Override
    public void onClick(View v) {
        showDropDown();
    }
}
