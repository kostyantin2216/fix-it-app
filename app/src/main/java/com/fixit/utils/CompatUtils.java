package com.fixit.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by Kostyantin on 8/17/2017.
 */

public class CompatUtils {

    public static Spanned fromHtml(String bodyData) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY);
        } else{
            return Html.fromHtml(bodyData);
        }
    }

}
