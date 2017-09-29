package com.fixit.general;

import android.content.Context;
import android.text.TextUtils;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fixit.data.OrderData;
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
    private final static String EVENT_SIGN_UP = FirebaseAnalytics.Event.SIGN_UP;
    private static final String EVENT_LOGIN = FirebaseAnalytics.Event.LOGIN;
    private final static String EVENT_SHOW_PROFESSIONS = "show_professions";
    private final static String EVENT_SHOW_MAP = "show_map";
    private final static String EVENT_SHOW_JOB_REASONS = "show_job_reasons";
    private final static String EVENT_TRADESMAN_SHOWN = "tradesman_shown";
    private final static String EVENT_TRADESMAN_SELECTED = "tradesman_selected";
    private final static String EVENT_RESULTS_SELECTED = "results_selected";
    private final static String EVENT_ORDER_CONFIRMED = "order_confirmed";
    private final static String EVENT_TRADESMEN_ORDERED = "tradesmen_ordered";
    private final static String EVENT_SHOW_ORDER_HISTORY = "show_order_history";
    private final static String EVENT_TRADESMAN_REVIEWED = "tradesman_reviewed";
    private final static String EVENT_TRADESMAN_CALLED = "call_tradesman";
    private final static String EVENT_TRADESMAN_MESSAGED = "msg_tradesman";

    private final static String PARAM_PROFESSION = "profession";
    private final static String PARAM_LOCATION = "location";
    private final static String PARAM_COMPANY_NAME = "company_name";
    private final static String PARAM_TRADESMAN_ID = "tradesman_id";
    private final static String PARAM_COUNT = "count";
    private final static String PARAM_TELEPHONE = "telephone";
    private final static String PARAM_JOB_REASON_COUNT = "job_reason_count";
    private final static String PARAM_PROVIDED_COMMENT = "provided_comment";
    private static final String PARAM_RATING = "rating";

    private final static String LEAD_VALUE = "50.00";
    private final static String LEAD_VALUE_CURRENCY = "ZAR";

    private final Answers mAnswers;
    private final FirebaseAnalytics mFirebaseAnalytics;

    public AnalyticsManager(Context context) {
        mAnswers = Answers.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setUserProperty(UP_IS_REGISTERED, String.valueOf(
                !TextUtils.isEmpty(GlobalPreferences.getUserId(context))
        ));
    }

    private void sendEvent(QuickEvent event) {
        if(!TextUtils.isEmpty(event.name)) {

            if(event.forAnswers) {
                CustomEvent answersEvent = new CustomEvent(event.name);
                for(Map.Entry<String, String> param : event.params.entrySet()) {
                    answersEvent.putCustomAttribute(param.getKey(), param.getValue());
                }

                mAnswers.logCustom(answersEvent);
            }

            if(event.forFirebase) {
                mFirebaseAnalytics.logEvent(event.name, CommonUtils.toBundle(event.params));
            }

        }
    }

    public void login(boolean newUser, String method) {
        QuickEvent event = new QuickEvent(newUser ? EVENT_SIGN_UP : EVENT_LOGIN)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.SIGN_UP_METHOD, method);
        sendEvent(event);
    }

    public void trackSearch(String profession, String location) {
        QuickEvent event = new QuickEvent(EVENT_SEARCH)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.SEARCH_TERM, profession)
                .addParam(FirebaseAnalytics.Param.DESTINATION, location);
        sendEvent(event);

        event = new QuickEvent(EVENT_SEARCH)
                .forAnswers()
                .addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_LOCATION, location);
        sendEvent(event);
    }

    public void trackSearchResults(String profession, String location, int count) {
        QuickEvent event = new QuickEvent(EVENT_VIEW_SEARCH_RESULTS)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.SEARCH_TERM, profession);
        sendEvent(event);

        event = new QuickEvent(EVENT_VIEW_SEARCH_RESULTS)
                .forAnswers()
                .addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_LOCATION, location)
                .addParam(PARAM_COUNT, String.valueOf(count));
        sendEvent(event);
    }

    public void trackResultsSelected(String profession, int count) {
        QuickEvent event = new QuickEvent(EVENT_RESULTS_SELECTED)
                .forAnswers()
                .addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_COUNT, String.valueOf(count));
        sendEvent(event);
    }

    public void trackShowProfessions() {
        trackViewItemList("professions");

        QuickEvent event = new QuickEvent(EVENT_SHOW_PROFESSIONS)
                .forAnswers();
        sendEvent(event);
    }

    public void trackShowMap() {
        trackViewItemList("map");

        QuickEvent event = new QuickEvent(EVENT_SHOW_MAP)
                .forAnswers();
        sendEvent(event);
    }

    public void trackShowReasons() {
        trackViewItemList("job_reasons");

        QuickEvent event = new QuickEvent(EVENT_SHOW_JOB_REASONS)
                .forAnswers();
        sendEvent(event);
    }

    public void trackTradesmanShown(Tradesman tradesman, String profession) {
        trackViewItem(tradesman.get_id(), tradesman.getCompanyName(), profession);

        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_SHOWN)
                .forAnswers()
                .addParam(PARAM_TRADESMAN_ID, tradesman.get_id())
                .addParam(PARAM_COMPANY_NAME, tradesman.getCompanyName())
                .addParam(PARAM_PROFESSION, profession);
        sendEvent(event);
    }

    public void trackTradesmanSelected(Tradesman tradesman, String profession) {
        trackContentSelect(profession, tradesman.get_id());

        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_SELECTED)
                .forAnswers()
                .addParam(PARAM_TRADESMAN_ID, tradesman.get_id())
                .addParam(PARAM_COMPANY_NAME, tradesman.getCompanyName())
                .addParam(PARAM_PROFESSION, profession);
        sendEvent(event);
    }

    public void trackOrderConfirmed(String profession, int tradesmenCount, int jobReasonCount, boolean commentProvided) {
        QuickEvent event = new QuickEvent(EVENT_ORDER_CONFIRMED)
                .forAnswers()
                .addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_COUNT,String.valueOf(tradesmenCount))
                .addParam(PARAM_JOB_REASON_COUNT, String.valueOf(jobReasonCount))
                .addParam(PARAM_PROVIDED_COMMENT, String.valueOf(commentProvided));
        sendEvent(event);
    }

    public void trackTradesmanOrder(String profession, int tradesmenCount) {
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.GENERATE_LEAD)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.CURRENCY, LEAD_VALUE_CURRENCY)
                .addParam(FirebaseAnalytics.Param.VALUE, LEAD_VALUE);
        sendEvent(event);

        event = new QuickEvent(EVENT_TRADESMEN_ORDERED)
                .forAnswers()
                .addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_COUNT, String.valueOf(tradesmenCount));
        sendEvent(event);
    }

    public void trackHistoryShown() {
        trackViewItemList("order_history");

        QuickEvent event = new QuickEvent(EVENT_SHOW_ORDER_HISTORY)
                .forAnswers();
        sendEvent(event);
    }

    public void trackTradesmanCallAction(String telephone, String companyName) {
        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_CALLED)
                .forAnswers()
                .addParam(PARAM_TELEPHONE, telephone)
                .addParam(PARAM_COMPANY_NAME, companyName);
        sendEvent(event);
    }

    public void trackMessageTradesmanAction(String telephone, String companyName) {
        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_MESSAGED)
                .forAnswers()
                .addParam(PARAM_TELEPHONE, telephone)
                .addParam(PARAM_COMPANY_NAME, companyName);
        sendEvent(event);
    }

    public void trackTradesmanReview(float rating, String tradesmanId, String companyName) {
        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_REVIEWED)
                .forAnswers()
                .addParam(PARAM_RATING, String.valueOf(rating))
                .addParam(PARAM_TRADESMAN_ID, tradesmanId)
                .addParam(PARAM_COMPANY_NAME, companyName);
        sendEvent(event);
    }

    private void trackContentSelect(String type, String id) {
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.SELECT_CONTENT)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.CONTENT_TYPE, type)
                .addParam(FirebaseAnalytics.Param.ITEM_ID, id);
        sendEvent(event);
    }

    private void trackViewItem(String id, String name, String category) {
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.VIEW_ITEM)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.ITEM_ID, id)
                .addParam(FirebaseAnalytics.Param.ITEM_NAME, name)
                .addParam(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        sendEvent(event);
    }

    private void trackViewItemList(String category) {
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        sendEvent(event);
    }

    public static class QuickEvent {
        private final String name;
        private final Map<String, String> params;

        private boolean forFirebase = false;
        private boolean forAnswers = false;

        public QuickEvent(String name) {
            this.name = name;
            this.params = new HashMap<>();
        }

        public QuickEvent forFirebase() {
            forFirebase = true;
            return this;
        }

        public QuickEvent forAnswers() {
            forAnswers = true;
            return this;
        }

        public QuickEvent addParam(String key, String value) {
            params.put(key, value);
            return this;
        }
    }

}
