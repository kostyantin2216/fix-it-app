package com.fixit.general;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.data.Tradesman;
import com.fixit.utils.CommonUtils;
import com.fixit.utils.GlobalPreferences;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kostyantin on 7/12/2017.
 */
public class AnalyticsManager {

    private final static String UP_IS_REGISTERED = "is_registered";

    private final static String DEFAULT_CURRENCY = "ZAR";

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
    private final static String EVENT_PERMISSION_REQUEST = "permission_request";
    private final static String EVENT_PERMISSION_DENIED = "permission_denied";
    private final static String EVENT_PERMISSION_GRANTED = "permission_granted";
    private final static String EVENT_SMS_VERIFICATION_REQUEST = "sms_verification_request";
    private final static String EVENT_SMS_VERIFICATION_COMPLETE = "sms_verification_complete";
    private final static String EVENT_SMS_VERIFICATION_ERROR = "sms_verification_error";

    private final static String PARAM_PROFESSION = "profession";
    private final static String PARAM_LOCATION = "location";
    private final static String PARAM_COMPANY_NAME = "company_name";
    private final static String PARAM_TRADESMAN_ID = "tradesman_id";
    private final static String PARAM_COUNT = "count";
    private final static String PARAM_TELEPHONE = "telephone";
    private final static String PARAM_JOB_REASON_COUNT = "job_reason_count";
    private final static String PARAM_PROVIDED_COMMENT = "provided_comment";
    private static final String PARAM_RATING = "rating";
    private final static String PARAM_PERMISSIONS = "permission";
    private final static String PARAM_MILLI_SECONDS = "milli_seconds";
    private final static String PARAM_ERROR = "error";
    private final static String PARAM_ERROR_CODE = "error_code";
    private final static String PARAM_PRICE = "price";

    private final static String LEAD_VALUE = "50.00";
    private final static String LEAD_VALUE_CURRENCY = "ZAR";

    private final Answers mAnswers;
    private final FirebaseAnalytics mFirebase;
    private final AppEventsLogger mFacebook;

    public AnalyticsManager(Context context) {
        mAnswers = Answers.getInstance();
        mFirebase = FirebaseAnalytics.getInstance(context);
        mFirebase.setUserProperty(UP_IS_REGISTERED, String.valueOf(
                !TextUtils.isEmpty(GlobalPreferences.getUserId(context))
        ));
        mFacebook = AppEventsLogger.newLogger(context);
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
                mFirebase.logEvent(event.name, CommonUtils.toBundle(event.params));
            }

            if(event.forAppsFlyer) {
                HashMap<String, Object> params = new HashMap<>();
                params.putAll(event.params);
                AppsFlyerLib.getInstance().trackEvent(event.context, event.name, params);
            }

            if(event.forFacebook) {
                Bundle params = new Bundle();
                double price = -1;
                String priceStr = event.params.remove(PARAM_PRICE);

                if(CommonUtils.isDecimal(priceStr)) {
                    price = Double.parseDouble(priceStr);
                }

                for(Map.Entry<String, String> entry : event.params.entrySet()) {
                    params.putString(entry.getKey(), entry.getValue());
                }

                if(price < 0) {
                    mFacebook.logEvent(event.name, params);
                } else {
                    mFacebook.logEvent(event.name, price, params);
                }
            }
        }
    }

    public void login(Context context, boolean newUser, String method, boolean fromNav) {
        if(fromNav) {
            method = "nav-" + method;
        }
        QuickEvent event = new QuickEvent(newUser ? EVENT_SIGN_UP : EVENT_LOGIN)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.SIGN_UP_METHOD, method);
        sendEvent(event);

        event = new QuickEvent(AFInAppEventType.COMPLETE_REGISTRATION)
                .forAppsFlyer(context)
                .addParam(AFInAppEventParameterName.REGSITRATION_METHOD, method);
        sendEvent(event);

        event = new QuickEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION)
                .forFacebook()
                .addParam(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, method);
        sendEvent(event);
    }

    public void trackSearch(Context context, String profession, String location) {
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

        event = new QuickEvent(AFInAppEventType.SEARCH)
                .forAppsFlyer(context)
                .addParam(AFInAppEventParameterName.CONTENT_TYPE, profession)
                .addParam(AFInAppEventParameterName.SEARCH_STRING, location);
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

        event = new QuickEvent(AppEventsConstants.EVENT_NAME_SEARCHED)
                .forFacebook()
                .addParam(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, profession)
                .addParam(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, location)
                .addParam(AppEventsConstants.EVENT_PARAM_SUCCESS, String.valueOf(count > 0));
        sendEvent(event);
    }

    public void trackResultsSelected(Context context, String profession, int count) {
        QuickEvent event = new QuickEvent(EVENT_RESULTS_SELECTED)
                .forAnswers()
                .addParam(PARAM_PROFESSION, profession)
                .addParam(PARAM_COUNT, String.valueOf(count));
        sendEvent(event);

        event = new QuickEvent(AFInAppEventType.INITIATED_CHECKOUT)
                .forAppsFlyer(context)
                .addParam(AFInAppEventParameterName.PRICE, String.valueOf(count))
                .addParam(AFInAppEventParameterName.CONTENT_TYPE, profession)
                .addParam(AFInAppEventParameterName.CURRENCY, DEFAULT_CURRENCY)
                .addParam(AFInAppEventParameterName.QUANTITY, String.valueOf(count))
                .addParam(AFInAppEventParameterName.PAYMENT_INFO_AVAILIBLE, String.valueOf(false));
        sendEvent(event);

        event = new QuickEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT)
                .forFacebook()
                .addParam(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, profession)
                .addParam(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, String.valueOf(count))
                .addParam(PARAM_PRICE, String.valueOf(count))
                .addParam(AppEventsConstants.EVENT_PARAM_CURRENCY, DEFAULT_CURRENCY);
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

    public void trackTradesmanSelected(Context context, Tradesman tradesman, String profession) {
        trackContentSelect(profession, tradesman.get_id());

        QuickEvent event = new QuickEvent(EVENT_TRADESMAN_SELECTED)
                .forAnswers()
                .addParam(PARAM_TRADESMAN_ID, tradesman.get_id())
                .addParam(PARAM_COMPANY_NAME, tradesman.getCompanyName())
                .addParam(PARAM_PROFESSION, profession);
        sendEvent(event);

        event = new QuickEvent(AFInAppEventType.ADD_TO_CART)
                .forAppsFlyer(context)
                .addParam(AFInAppEventParameterName.PRICE, "1")
                .addParam(AFInAppEventParameterName.CONTENT_TYPE, profession)
                .addParam(AFInAppEventParameterName.CONTENT_ID, tradesman.get_id())
                .addParam(AFInAppEventParameterName.CURRENCY, DEFAULT_CURRENCY)
                .addParam(AFInAppEventParameterName.QUANTITY, "1");
        sendEvent(event);

        event = new QuickEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART)
                .forFacebook()
                .addParam(PARAM_PRICE, "1")
                .addParam(AppEventsConstants.EVENT_PARAM_CURRENCY, DEFAULT_CURRENCY)
                .addParam(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, profession)
                .addParam(AppEventsConstants.EVENT_PARAM_CONTENT_ID, tradesman.get_id());
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

    public void trackTradesmanOrdered(Context context, Profession profession, OrderData order) {
        int tradesmenCount = order.getTradesmen().length;
        QuickEvent event = new QuickEvent(FirebaseAnalytics.Event.GENERATE_LEAD)
                .forFirebase()
                .addParam(FirebaseAnalytics.Param.CURRENCY, LEAD_VALUE_CURRENCY)
                .addParam(FirebaseAnalytics.Param.VALUE, LEAD_VALUE);
        sendEvent(event);

        event = new QuickEvent(EVENT_TRADESMEN_ORDERED)
                .forAnswers()
                .addParam(PARAM_PROFESSION, profession.getName())
                .addParam(PARAM_COUNT, String.valueOf(tradesmenCount));
        sendEvent(event);

        event = new QuickEvent(AFInAppEventType.PURCHASE)
                .forAppsFlyer(context)
                .addParam(AFInAppEventParameterName.REVENUE, String.valueOf(tradesmenCount * 20))
                .addParam(AFInAppEventParameterName.CONTENT_TYPE, profession.getName())
                .addParam(AFInAppEventParameterName.CONTENT_ID, String.valueOf(profession.getId()))
                .addParam(AFInAppEventParameterName.PRICE, String.valueOf(tradesmenCount))
                .addParam(AFInAppEventParameterName.CURRENCY, DEFAULT_CURRENCY)
                .addParam(AFInAppEventParameterName.QUANTITY, String.valueOf(tradesmenCount))
                .addParam("af_order_id", order.get_id());
        sendEvent(event);

        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, tradesmenCount);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, profession.getName());
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, String.valueOf(profession.getId()));
        mFacebook.logPurchase(new BigDecimal(tradesmenCount * 20), Currency.getInstance(DEFAULT_CURRENCY), params);
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
        
    }

    public void trackPermissionRequest(String[] permissions) {
        QuickEvent event = new QuickEvent(EVENT_PERMISSION_REQUEST)
                .forAnswers();
        trackPermissions(event, permissions);
    }

    public void trackPermissionGranted(String[] permissions) {
        QuickEvent event = new QuickEvent(EVENT_PERMISSION_GRANTED)
                .forAnswers();
        trackPermissions(event, permissions);
    }

    public void trackPermissionDenied(String[] permissions) {
        QuickEvent event = new QuickEvent(EVENT_PERMISSION_DENIED)
                .forAnswers();
        trackPermissions(event, permissions);
    }

    public void trackSmsVerificationRequest(String number) {
        QuickEvent event = new QuickEvent(EVENT_SMS_VERIFICATION_REQUEST)
                .forAnswers()
                .addParam(PARAM_TELEPHONE, number);
        sendEvent(event);
    }

    public void trackSmsVerificationComplete(String number, long timeMillis) {
        QuickEvent event = new QuickEvent(EVENT_SMS_VERIFICATION_COMPLETE)
                .forAnswers()
                .addParam(PARAM_TELEPHONE, number)
                .addParam(PARAM_MILLI_SECONDS, String.valueOf(timeMillis));
        sendEvent(event);
    }

    public void trackSmsVerificationError(String number, int errorCode, String error) {
        QuickEvent event = new QuickEvent(EVENT_SMS_VERIFICATION_ERROR)
                .forAnswers()
                .addParam(PARAM_TELEPHONE, number)
                .addParam(PARAM_ERROR_CODE, String.valueOf(errorCode))
                .addParam(PARAM_ERROR, error);
        sendEvent(event);
    }

    private void trackPermissions(QuickEvent event, String[] permissions) {
        String permissionsStr;
        switch (permissions.length) {
            case 0:
                permissionsStr = "NA";
                break;
            case 1:
                permissionsStr = permissions[0];
                break;
            default:
                Arrays.sort(permissions);
                permissionsStr = Arrays.toString(permissions);
        }
        event.addParam(PARAM_PERMISSIONS, permissionsStr);
        sendEvent(event);
    }

    public static class QuickEvent {
        private final String name;
        private final Map<String, String> params;

        private Context context;

        private boolean forFirebase = false;
        private boolean forAnswers = false;
        private boolean forAppsFlyer = false;
        private boolean forFacebook = false;

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

        public QuickEvent forAppsFlyer(Context context) {
            this.context = context;
            forAppsFlyer = true;
            return this;
        }

        public QuickEvent forFacebook() {
            forFacebook = true;
            return this;
        }

        public QuickEvent addParam(String key, String value) {
            params.put(key, value);
            return this;
        }
    }

}
