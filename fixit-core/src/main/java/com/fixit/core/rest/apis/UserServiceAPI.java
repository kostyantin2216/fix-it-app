package com.fixit.core.rest.apis;

import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.requests.data.TelephoneVerificationRequestData;
import com.fixit.core.rest.requests.data.UserRegistrationRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.TelephoneVerificationResponseData;
import com.fixit.core.rest.responses.data.UserRegistrationResponseData;
import com.fixit.core.rest.services.UserServiceService;

import retrofit2.Callback;

/**
 * Created by konstantin on 5/15/2017.
 */

public class UserServiceAPI extends BaseServiceAPI {

    public final static String API_NAME = "services/users";

    private final UserServiceService mService;

    public UserServiceAPI(APIRequestHeader header, UserServiceService service) {
        super(header);
        mService = service;
    }

    public void registerUser(UserRegistrationRequestData requestData, Callback<APIResponse<UserRegistrationResponseData>> callback) {
        mService.registerUser(new APIRequest<>(mHeader, requestData)).enqueue(callback);
    }

    public void verifyTelephone(TelephoneVerificationRequestData requestData, Callback<APIResponse<TelephoneVerificationResponseData>> callback) {
        mService.verifyTelephone(new APIRequest<>(mHeader, requestData)).enqueue(callback);
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
