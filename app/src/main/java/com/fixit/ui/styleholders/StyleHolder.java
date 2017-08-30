package com.fixit.ui.styleholders;

import android.view.View;

/**
 * Created by Kostyantin on 5/25/2017.
 */

public interface StyleHolder<V extends View> {
    void applyStyle(V view);
}
