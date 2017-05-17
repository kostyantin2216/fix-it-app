package com.fixit.core.rest.services;

import com.fixit.core.rest.apis.twilio.TwilioOutcome;
import com.fixit.core.rest.responses.TwilioMessageResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by konstantin on 5/16/2017.
 */

public interface TwilioExternalService {

    @FormUrlEncoded
    @POST("Accounts/{accountSid}/Messages.json")
    Call<TwilioMessageResponse> sendMessage(@Path("accountSid") String accountSid,
                                            @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("Accounts/{accountSid}/Messages/{messageSid}/Feedback")
    Call<Void> provideMessageFeedback(@Path("accountSid") String accountSid,
                                      @Path("messageSid") String messageSid,
                                      @Field("Outcome") String outcome);

}
