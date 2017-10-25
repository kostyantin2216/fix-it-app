package com.fixit.ui.helpers;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

/**
 * Created by Kostyantin on 10/25/2017.
 */

public class SoftKeyboardHandler {

    private final Listener mListener;
    private final ViewGroup mRootLayout;
    private final ViewTreeObserver.OnGlobalLayoutListener mKeyboardLayoutListener;

    private boolean mKeyboardLayoutListenerAttached = false;

    public SoftKeyboardHandler(final Window window, ViewGroup rootLayout, Listener listener) {
        mRootLayout = rootLayout;
        mListener = listener;
        mKeyboardLayoutListener = () -> {
            int heightDiff = mRootLayout.getRootView().getHeight() - mRootLayout.getHeight();
            int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();

            if (heightDiff <= contentViewTop) {
                mListener.onHideKeyboard();
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                mListener.onShowKeyboard(keyboardHeight);
            }
        };
    }

    protected void attachKeyboardListeners() {
        if(!mKeyboardLayoutListenerAttached) {
            mRootLayout.getViewTreeObserver().addOnGlobalLayoutListener(mKeyboardLayoutListener);
            mKeyboardLayoutListenerAttached = true;
        }
    }

    protected void detachKeyboardLayoutListeners() {
        if(mKeyboardLayoutListenerAttached) {
            mRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mKeyboardLayoutListener);
            mKeyboardLayoutListenerAttached = false;
        }
    }

    public interface Listener {
        void onShowKeyboard(int keyboardHeight);
        void onHideKeyboard();
    }

}
