package com.fixit.general;

import android.content.Context;
import android.text.TextUtils;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fixit.data.Tradesman;
import com.fixit.utils.CommonUtils;
import com.fixit.utils.GlobalPreferences;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kostyantin on 7/12/2017.
 */
public class AnalyticsManager {

    private final static String UP_IS_REGISTERED = "is_registered";

    private final static String EVENT_SEARCH = FirebaseAnalytics.Event.SEARCH;
    private final static String EVENT_VIEW_SEARCH_RESULTS = FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS;
    private final static String EVENT_SHOW_PROFESSIONS = "show_professions";
    private final static String EVENT_SHOW_MAP = "show_map";
    private final static String EVENT_SHOW_JOB_REASONS = "show_job_reasons";
    private final static String EVENT_JOB_REASONS = "job_reasons";
    private final static String EVENT_TRADESMAN_SHOWN = "tradesman_shown";
    private final static String EVENT_TRADESMAN_SELECTED = "tradesman_selected";
    private final static String EVENT_RESULTS_SELECTED = "results_selected";

    private final static String PARAM_PROFESSION = "profession";
    private final static String PARAM_LOCATION = "location";
    private final static String PARAM_COMPANY_NAME = "company_name";
    private final static String PARAM_TRADESMAN_ID = "tradesman_id";
    private final static String PARAM_COUNT = "count";
    private final static String PARAM_CUSTOM_TEXT = "custom_text";

    private final Answers mAnswers;
    private final FirebaseAnalytics mFirebaseAnalytics;

    public AnalyticsManager(Context context) {
        mAnswers = Answers.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setUserProperty(UP_IS_REGISTERED, String.valueOf(
                !TextUtils.isEmpty(GlobalPreferences.getUserId(context))
        ));
    }

    private void sendEvent(QuickEvent event, boolean sendToAnswers, boolean sendToFirebase) {
        if(!TextUtils.isEmpty(event.name)) {

            if(sendToAnswers) {
                CustomEvent answersEvent = new CustomEvent(event.name);
                for(Map.Entry<String, String> param : event.params.entrySet()) {
                    answersEvent.putCustomAttribute(param.getKey(), param.getValue());
                }

                mAnswers.logCustom(answersEvent);
            }

            if(sendToFirebase) {
                mFirebaseAnalytics.logEvent(event.name, CommonUtils.toBundle(event.params));
            }

        }
    }

    public void trackSearch(String profession, String location) {
        QuickEvent event = new QuickEvent(EVENT_SEARCH)
                .addParam(FirebaseAnalytics.Param.SEARCH_TERM, profession)
                .addParam(FirebaseAnalytics.Param.DESTINATION, location);
        sendEvent(event, false, true);

        event.params.clear();

        event.addParam(PARAM_PROFESSION, profession)
             .addParam(PARAM_LOCATION, location);
        sendEvent(event, true, false);
    }

    public void trackSearchResults(String profession, String location, int count) {
        QuickEvent event = new QuickEvent(EVENT_VIEW_SEARCH_RESULTS)
                .addParam(FirebaseAnalytics.Param.SEARCH_TERM, profession);
        sendEvent(event, false, true);

        event.params.clear();

        event.addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_LOCATION, location)
                .addParam(PARAM_COUNT, String.valueOf(count));
        sendEvent(event, true, false);
    }

    public void trackResultsSelected(String profession, int count) {
        QuickEvent event = new QuickEvent(EVENT_RESULTS_SELECTED)
                .addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_COUNT, String.valueOf(count));
        sendEvent(event, true, false);
    }

    public void trackShowProfessions() {
        trackViewItemList("professions");

        QuickEvent event = new QuickEvent(EVENT_SHOW_PROFESSIONS);
        sendEvent(event, true, false);
    }

    public void trackShowMap() {
        trackViewItemList("map");

        QuickEvent event = new QuickEvent(EVENT_SHOW_MAP);
        sendEvent(event, true, false);
    }

    public void trackShowReasons() {
        trackViewItemList("job_reasons");

        QuickEvent event = new QuickEvent(EVENT_SHOW_JOB_REASONS);
        sendEvent(event, true, false);
    }

    public void trackJobReasonsSelected(boolean customText, int count) {
        QuickEvent event = new QuickEvent(EVENT_JOB_REASONS)
                .addParam(PARAM_CUSTOM_TEXT, String.valueOf(customText))
                .addParam(PARAM_COUNT, String.valueOf(count));
        sendEvent(event, true, false);
    }

    public void trackTradesmanShown(Tradesman tradesman, String profession) {
        trackViewItem(tradesman.get_id(), tradesman.getCompanyName(), profession);

        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_SHOWN)
                .addParam(PARAM_TRADESMAN_ID, tradesman.get_id())
                .addParam(PARAM_COMPANY_NAME, tradesman.getCompanyName())
                .addParam(PARAM_PROFESSION, profession);
        sendEvent(event, true, false);
    }

    public void trackTradesmanSelected(Tradesman tradesman, String profession) {
        trackContentSelect(profession, tradesman.get_id());

        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_SELECTED)
                .addParam(PARAM_TRADESMAN_ID, tradesman.get_id())
                .addParam(PARAM_COMPANY_NAME, tradesman.getCompanyName())
                .addParam(PARAM_PROFESSION, profession);
        sendEvent(event, true, false);
    }

    private void trackContentSelect(String type, String id) {
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.SELECT_CONTENT)
                .addParam(FirebaseAnalytics.Param.CONTENT_TYPE, type)
                .addParam(FirebaseAnalytics.Param.ITEM_ID, id);
        sendEvent(event, false, true);
    }

    private void trackViewItem(String id, String name, String category) {
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.VIEW_ITEM)
                .addParam(FirebaseAnalytics.Param.ITEM_ID, id)
                .addParam(FirebaseAnalytics.Param.ITEM_NAME, name)
                .addParam(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        sendEvent(event, false, true);
    }

    private void trackViewItemList(String category) {
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST)
                .addParam(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        sendEvent(event, false, true);
    }

    public static class QuickEvent {
        private final String name;
        private final Map<String, String> params;

        public QuickEvent(String name) {
            this.name = name;
            this.params = new HashMap<>();
        }

        public QuickEvent addParam(String key, String value) {
            params.put(key, value);
            return this;
        }
    }

}
