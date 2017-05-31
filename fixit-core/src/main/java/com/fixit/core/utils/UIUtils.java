package com.fixit.core.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.fixit.core.data.SimpleDisplayMetrics;

/**
 * Created by konstantin on 4/27/2017.
 */

public class UIUtils {

    public static int dpToPx(Context context, int dp) {
        int px = Math.round(dp * getPixelScaleFactor(context));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        int dp = Math.round(px / getPixelScaleFactor(context));
        return dp;
    }

    public static float pxToSp(Context context, int px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    public static int spToPx(Context context, float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static long expand(View v, long durationMultiplier) {
        return expand(v, durationMultiplier, null);
    }

    public static long expand(final View v, long durationMultiplier, View indicator) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        // Setting height to targetHeight makes view jump to full height before expanding which causes a weird effect.
        // If setting to 1 does not work for all devices then maybe check api version before setting height.
        v.getLayoutParams().height = 1;
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density) * durationMultiplier);
        v.startAnimation(a);
        long duration = a.getDuration();
        if(indicator != null) {
            rotate180Degrees(indicator, false, duration);
        }
        return duration;
    }

    public static long collapse(View v, long durationMultiplier) {
        return collapse(v, durationMultiplier, null);
    }

    public static long collapse(final View v, long durationMultiplier, View indicator) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density) * durationMultiplier);
        v.startAnimation(a);

        long duration = a.getDuration();
        if(indicator != null) {
            rotate180Degrees(indicator, true, duration);
        }
        return duration;
    }

    public static void rotate180Degrees(View v, boolean fromTop) {
        rotate180Degrees(v, fromTop, 120);
    }

    public static void rotate180Degrees(View v, boolean fromTop, long duration) {
        RotateAnimation r = new RotateAnimation(
                fromTop ? 0 : 180,
                fromTop ? 180 : 360,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        r.setInterpolator(new FastOutLinearInInterpolator());
        r.setDuration(duration);
        r.setRepeatCount(0);
        r.setFillAfter(true);
        v.startAnimation(r);
    }
    // http://easings.net/
    private final static Interpolator easeInOutQuart = PathInterpolatorCompat.create(0.77f, 0f, 0.175f, 1f);

    public static Animation expand(final View view) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) view.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = view.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0 so use 1 instead.
        view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);

                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setInterpolator(easeInOutQuart);
        animation.setDuration(computeDurationFromHeight(view));
        view.startAnimation(animation);

        return animation;
    }

    public static Animation collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setInterpolator(easeInOutQuart);

        int durationMillis = computeDurationFromHeight(view);
        a.setDuration(durationMillis);

        view.startAnimation(a);

        return a;
    }

    private static int computeDurationFromHeight(View view) {
        // 1dp/ms * multiplier
        return (int) (view.getMeasuredHeight() / view.getContext().getResources().getDisplayMetrics().density);
    }

    /**
     *  This only checks if the whole view is visible on the screen vertically not horizontally.
     * @param view
     * @return
     */
    public static boolean isWholeViewVisibleOnScreen(View view) {
        Context context = view.getContext();
        SimpleDisplayMetrics screenSize = getScreenSize(context);
        int[] locationOnScreen = new int[2];
        view.getLocationOnScreen(locationOnScreen);

        int viewHeight = view.getHeight();

        return (locationOnScreen[1] + viewHeight) < screenSize.getHeight();
    }

    private static SimpleDisplayMetrics screenSize;
    /**
     * @param context
     * @return an array containing width and height of the screen in that order.
     */
    public static SimpleDisplayMetrics getScreenSize(Context context) {
        if(screenSize == null) {
            int measuredWidth = 0;
            int measuredHeight = 0;
            WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Point size = new Point();
                w.getDefaultDisplay().getSize(size);
                measuredWidth = size.x;
                measuredHeight = size.y;
            } else {
                Display d = w.getDefaultDisplay();
                measuredWidth = d.getWidth();
                measuredHeight = d.getHeight();
            }

            screenSize = new SimpleDisplayMetrics(measuredWidth, measuredHeight);
        }
        return screenSize;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
