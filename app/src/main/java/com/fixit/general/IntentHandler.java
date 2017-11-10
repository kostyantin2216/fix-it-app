package com.fixit.general;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.fixit.controllers.SearchController;
import com.fixit.data.Profession;
import com.fixit.ui.activities.SplashActivity;
import com.fixit.ui.activities.SplitSearchActivity;
import com.fixit.utils.CommonUtils;

import java.util.List;

/**
 * Created by Kostyantin on 11/7/2017.
 */

public class IntentHandler {

    private final static String HOST_SEARCH = "search";

    private final static String PARAM_PROFESSION = "profession";

    // General handler.
    public static boolean handle(SplashActivity activity) {
        Intent intent = activity.getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if(isValid(action, data)) {
            printData(data);

            if(isSearchIntent(data)) {
                activity.startActivity(intent.setClass(activity, SplitSearchActivity.class));
                activity.finish();
                return true;
            }
        }
        return false;
    }

    // Search handler.
    public static boolean handle(SplitSearchActivity activity) {
        Intent intent = activity.getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if(isValid(action, data)) {
            printData(data);
            if (isSearchIntent(data)) {

                String professionParam = data.getQueryParameter(PARAM_PROFESSION);
                if(TextUtils.isEmpty(professionParam)) {
                    List<String> pathSegments = data.getPathSegments();
                    if(!pathSegments.isEmpty()) {
                        professionParam = pathSegments.get(pathSegments.size() - 1);
                    }
                }

                professionParam = professionParam.replace("_", " ");

                if(!TextUtils.isEmpty(professionParam)) {
                    SearchController searchController = activity.getController();
                    if (searchController != null) {
                        Profession profession = searchController.getProfession(professionParam);
                        if(profession != null) {
                            activity.onProfessionSelected(profession);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSearchIntent(Uri data) {
        if(data.getHost().equals(HOST_SEARCH)) {
            return true;
        }

        List<String> pathSegments = data.getPathSegments();
        return pathSegments.contains(HOST_SEARCH);
    }

    private static boolean isValid(String action, Uri data) {
        return action != null && data != null && action.equals(Intent.ACTION_VIEW);
    }

    private static void printData(Uri data) {
        String t = "#LINK";
        Log.i(t, "data: " + data);
        Log.i(t, "host: " + data.getHost());
        Log.i(t, "path: " + data.getPath());
        Log.i(t, "query: " + data.getQuery());
        Log.i(t, "path segments: " + CommonUtils.toString(data.getPathSegments()));
    }

}
