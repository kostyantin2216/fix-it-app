package com.fixit.ui.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.fixit.app.R;
import com.fixit.utils.FILog;
import com.popalay.tutors.TutorialListener;
import com.popalay.tutors.Tutors;
import com.popalay.tutors.TutorsBuilder;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Kostyantin on 10/24/2017.
 */

public class UITutorials {

    private final static String LOG_TAG = "#UITUTORIALS";

    private final static String PREF_GROUP_TUTORIALS = "tutorial_prefs";

    private final static Tutors TUTORS = new TutorsBuilder()
            .textColorRes(android.R.color.white)
            .shadowColorRes(R.color.tutors_shadow)
            .textSizeRes(R.dimen.tutors_textNormal)
            .completeIconRes(R.drawable.ic_check_white_24dp)
            .spacingRes(R.dimen.tutors_spacingNormal)
            .lineWidthRes(R.dimen.tutors_lineWidth)
            .cancelable(false)
            .build();

    public final static String TUTORIAL_SEARCH_SCREEN = "tutorial_search_screen";
    public final static String TUTORIAL_SEARCH_RESULTS = "tutorial_search_results";
    public final static String TUTORIAL_SELECT_TRADESMAN = "tutorial_select_tradesman";
    public final static String TUTORIAL_ORDER_SELECTED_TRADESMEN = "tutorial_order_selected_tradesmen";
    public final static String TUTORIAL_ORDER_DETAILS_SCREEN = "tutorial_order_confirmation_screen";

    public static Tutorial create(String name, View forView, String withDescription) {
        FILog.i(LOG_TAG, "creating " + name);
        return new Tutorial(name, new Step(forView, withDescription));
    }

    private static void show(final FragmentManager fm, final Tutorial tutorial) {
        FILog.i(LOG_TAG, "showing: " + tutorial.name);
        Context context = tutorial.steps.peek().forView.getContext();
        if(!isTutorialComplete(tutorial.name, context)) {
            showRecursive(context, fm, tutorial);
        }
    }

    private static void showRecursive(final Context context, final FragmentManager fm, final Tutorial tutorial) {
        Step step = tutorial.steps.poll();
        final boolean isLast = tutorial.steps.isEmpty();

        TUTORS.show(fm, step.forView, step.withDescription, isLast);

        FILog.i(LOG_TAG, tutorial.steps.size() + " tutorials left...");
        TUTORS.setListener(new TutorialListener() {
            @Override
            public void onNext() {
                showRecursive(context, fm, tutorial);
            }

            @Override
            public void onComplete() {
                completeTutorial(tutorial.name, context);
            }

            @Override
            public void onCompleteAll() {
                completeTutorial(tutorial.name, context);
            }
        });
    }

    private static void completeTutorial(final String name, final Context context) {
        FILog.i(LOG_TAG, name + " complete!");
        setTutorialComplete(name, context);
        TUTORS.setListener(null);
        TUTORS.close();
    }

    private static boolean isTutorialComplete(String name, Context context) {
        boolean isComplete = context.getSharedPreferences(PREF_GROUP_TUTORIALS, Context.MODE_PRIVATE)
                .getBoolean(name, false);
        FILog.i(LOG_TAG, name + " complete " + isComplete);
        return isComplete;
    }

    private static void setTutorialComplete(String name, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_GROUP_TUTORIALS, Context.MODE_PRIVATE)
                .edit().putBoolean(name, true);
        editor.apply();
    }

    public static class Tutorial {
        private final String name;
        private final Queue<Step> steps = new ArrayDeque<>();

        private Tutorial(String name, Step step) {
            this.name = name;
            steps.add(step);
        }

        public Tutorial and(View view, String description) {
            steps.add(new Step(view, description));
            return this;
        }

        public void show(FragmentManager fm) {
            UITutorials.show(fm, this);
        }
    }

    private static class Step {
        private final View forView;
        private final String withDescription;

        private Step(View v, String d) {
            this.forView = v;
            this.withDescription = d;
        }
    }

}
