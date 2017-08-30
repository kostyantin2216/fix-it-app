package com.fixit.rest.apis.twilio;

import android.content.Context;
import android.support.annotation.StringRes;

import com.fixit.app.R;
import com.fixit.rest.callbacks.EmptyCallback;
import com.fixit.rest.responses.TwilioMessageResponse;
import com.fixit.rest.services.TwilioExternalService;
import com.fixit.utils.FILog;

import java.util.Map;

import retrofit2.Callback;

/**
 * Created by konstantin on 5/16/2017.
 */

public class TwilioAPI {

    private final static String LOG_TAG = "TwilioAPI";

    private final TwilioExternalService mService;
    private final String mAccountSid;

    public TwilioAPI(TwilioExternalService service, String accountSid) {
        mService = service;
        this.mAccountSid = accountSid;
    }

    public void sendMessage(TwilioMessage message, Callback<TwilioMessageResponse> callback) {
        Map<String, String> fieldMap = message.toFieldMap();
        mService.sendMessage(mAccountSid, fieldMap).enqueue(callback);
    }

    public void provideMessageFeedback(String messageSid, TwilioOutcome outcome) {
        mService.provideMessageFeedback(mAccountSid, messageSid, outcome.name().toLowerCase()).enqueue(new EmptyCallback<Void>());
    }

    public String resolveErrorMessage(Context context, int errorCode) {
        return resolveErrorMessage(context, errorCode, null);
    }

    public String resolveErrorMessage(Context context, int errorCode, String fromNumber) {
        Error error = Error.findByCode(errorCode);
        if(error != null) {
            if(error == Error.FROM_BLACKLISTED) {
                return context.getString(error.messageId, fromNumber);
            } else {
                return context.getString(error.messageId);
            }
        }
        FILog.w(LOG_TAG, "could not resolve error(" + errorCode + ") for twilio", context);
        return context.getString(R.string.error_unknown);
    }

    private enum Error {

        INVALID_TO_NUMBER(21211, R.string.invalid_telephone),
        FROM_BLACKLISTED(21610, R.string.from_number_blacklisted),
        UNREACHABLE_NUMBER(21612, R.string.telephone_unreachable),
        INVALID_MOBILE_NUMBER(21614, R.string.invalid_mobile_number);

        final int code;
        @StringRes
        final int messageId;

        Error(int code, @StringRes int messageId) {
            this.code = code;
            this.messageId = messageId;
        }

        static Error findByCode(int code) {
            for(Error error : values()) {
                if(error.code == code) {
                    return error;
                }
            }
            return null;
        }
    }

}
