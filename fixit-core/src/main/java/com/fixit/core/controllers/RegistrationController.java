package com.fixit.core.controllers;

import com.fixit.core.BaseApplication;
import com.fixit.core.R;
import com.fixit.core.config.AppConfig;
import com.fixit.core.general.UnexpectedErrorCallback;
import com.fixit.core.rest.apis.twilio.TwilioAPI;
import com.fixit.core.rest.apis.twilio.TwilioMessage;
import com.fixit.core.rest.apis.twilio.TwilioOutcome;
import com.fixit.core.rest.callbacks.RetryingCallback;
import com.fixit.core.rest.callbacks.TwilioCallback;
import com.fixit.core.rest.responses.TwilioErrorResponse;
import com.fixit.core.rest.responses.TwilioMessageResponse;
import com.fixit.core.utils.FILog;

import java.security.SecureRandom;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by konstantin on 5/16/2017.
 */

public class RegistrationController extends UserController {

    private final TwilioAPI mTwilioApi;
    private final SecureRandom mRandom;
    private final String fromTelephone;

    private String previousMsgSid;

    public RegistrationController(BaseApplication baseApplication) {
        super(baseApplication);

        mTwilioApi = getServerApiFactory().createTwilioApi(baseApplication);
        mRandom = new SecureRandom();
        fromTelephone = AppConfig.getString(baseApplication, AppConfig.KEY_VERIFICATION_FROM_TELEPHONE, "");
    }

    public void verifyTelephone(String telephone, final TelephoneVerificationCallback callback) {
        final int verificationCode = generateVerificationCode();
        String body = getApplicationContext().getString(R.string.verification_message_body, verificationCode);
        TwilioMessage message = new TwilioMessage.Builder(fromTelephone, telephone, body).provideFeedback().build();
        mTwilioApi.sendMessage(message, new TwilioCallback<TwilioMessageResponse>(getApplicationContext()) {
            @Override
            public void onResponse(Call<TwilioMessageResponse> call, TwilioMessageResponse response) {
                previousMsgSid = response.getSid();
                callback.onVerificationCodeSent(verificationCode);
            }

            @Override
            public void onError(Call<TwilioMessageResponse> call, TwilioErrorResponse errorResponse) {
                int errorCode = errorResponse.getCode();
                callback.onVerificationError(errorCode,
                        mTwilioApi.resolveErrorMessage(getApplicationContext(), errorCode, fromTelephone)
                );
            }

            @Override
            public void onRetryFailure(Call<TwilioMessageResponse> call, Throwable t) {
                callback.onUnexpectedErrorOccurred("could not send verification code using twilio", t);
            }
        });
    }

    public void numberVerified() {
        mTwilioApi.provideMessageFeedback(previousMsgSid, TwilioOutcome.CONFIRMED);
    }

    private int generateVerificationCode() {
        return 100000 + mRandom.nextInt(900000);
    }

    public interface TelephoneVerificationCallback extends UnexpectedErrorCallback {
        void onVerificationCodeSent(int verificationCode);
        void onVerificationError(int code, String error);
    }

}
