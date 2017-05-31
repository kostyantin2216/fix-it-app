package com.fixit.app.ui.styleholders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.Button;

import com.fixit.app.R;
import com.fixit.core.ui.styleholders.StyleHolder;

/**
 * Created by Kostyantin on 5/25/2017.
 */

public class ButtonStyleHolder implements StyleHolder<Button> {

    private final Drawable background;
    private final ColorStateList textColor;
    private final int textSize;

    public ButtonStyleHolder(Context context) {
        TypedArray ta = context.obtainStyledAttributes(R.style.button, R.styleable.ButtonStyleHolder);

        Resources resources = context.getResources();

        background = ta.getDrawable(ta.getIndex(R.styleable.ButtonStyleHolder_android_background));

        textColor = ta.getColorStateList(ta.getIndex(R.styleable.ButtonStyleHolder_android_textColor));

        textSize = ta.getDimensionPixelSize(
                ta.getIndex(R.styleable.ButtonStyleHolder_android_textSize),
                resources.getDimensionPixelSize(R.dimen.standard_text_size)
        );

        ta.recycle();
    }

    @Override
    public void applyStyle(Button btn) {
        btn.setBackground(background);
        btn.setTextColor(textColor);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}
