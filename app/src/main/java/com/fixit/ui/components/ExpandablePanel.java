package com.fixit.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.utils.UIUtils;

/**
 * Created by Kostyantin on 5/27/2017.
 */
public class ExpandablePanel extends ConstraintLayout
        implements ToggleTextView.ToggleTextViewListener {

    private final TextView tvTitle;
    private final ToggleTextView tvToggle;
    private final FrameLayout panelBody;

    private ExpandablePanelListener mListener;

    public ExpandablePanel(@NonNull Context context) {
        this(context, null);
    }

    public ExpandablePanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandablePanel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.component_expandable_panel, this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        panelBody = (FrameLayout) findViewById(R.id.panel_body);

        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandablePanel);

            int bodyLayout = ta.getResourceId(R.styleable.ExpandablePanel_body, 0);
            if(bodyLayout > 0) {
                inflatePanelBody(bodyLayout);
            }

            ta.recycle();
        }

        tvToggle = (ToggleTextView) findViewById(R.id.tv_toggle);
        onToggle(tvToggle.isOn());

        if(!isInEditMode()) {
            tvToggle.setToggleListener(this);
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setListener(ExpandablePanelListener listener) {
        mListener = listener;
    }

    public View inflatePanelBody(@LayoutRes int panelBodyLayoutRes) {
        return LayoutInflater.from(getContext()).inflate(panelBodyLayoutRes, panelBody);
    }

    public void toggle() {
        tvToggle.toggle();
    }

    public void expand() {
        if(!tvToggle.isOn()) {
            toggle();
        }
    }

    public void collapse() {
        if(tvToggle.isOn()) {
            toggle();
        }
    }

    @Override
    public void onToggle(boolean on) {
        if(on) {
            if(panelBody.getVisibility() != VISIBLE) {
                UIUtils.expand(panelBody);
                if(mListener != null) {
                    mListener.onPanelExpanded(this);
                }
            }
        } else {
            if(panelBody.getVisibility() == VISIBLE) {
                UIUtils.collapse(panelBody);
                if(mListener != null) {
                    mListener.onPanelCollapsed(this);
                }
            }
        }
    }

    public interface ExpandablePanelListener {
        void onPanelExpanded(ExpandablePanel panel);
        void onPanelCollapsed(ExpandablePanel panel);
    }

}
