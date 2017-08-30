package com.fixit.caching;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixit.utils.FILog;

import java.util.Hashtable;

/**
 * Created by Kostyantin on 6/22/2017.
 */

public class FontCache {

    private final static String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";

    private final static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public enum CachedTypeface {
        STANDARD(ROBOTO_MEDIUM);

        final String path;
        CachedTypeface(String path) {
            this.path = path;
        }

        Typeface getTypeface(Context context) {
            return get(path, context);
        }
    }

    private static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

    private static void setTypeface(Typeface typeface, View v) {
        if(v instanceof TextView) {
            ((TextView) v).setTypeface(typeface);
        } else if(v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            int childCount = vg.getChildCount();
            for(int i = 0; i < childCount; i++) {
                setTypeface(typeface, vg.getChildAt(i));
            }
        } else {
            FILog.w("Cannot set typeface on view type " + v.getClass().getName());
        }
    }

    public static void setTypefaceRecursive(View v, CachedTypeface cachedTypeface) {
        setTypeface(cachedTypeface.getTypeface(v.getContext()), v);
    }

}
