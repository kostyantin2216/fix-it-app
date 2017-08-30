package com.fixit.rest.apis;

import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.APIRequestHeader;
import com.fixit.rest.requests.data.UserRegistrationRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.UserRegistrationResponseData;
import com.fixit.rest.services.UserService;

import retrofit2.Callback;

/**
 * Created by konstantin on 5/15/2017.
 */

public class UserServiceAPI extends BaseServiceAPI {

    public final static String API_NAME = "services/users";

    private final UserService mService;

    public UserServiceAPI(APIRequestHeader header, UserService service) {
        super(header);
        mService = service;
    }

    public void registerUser(UserRegistrationRequestData requestData, Callback<APIResponse<UserRegistrationResponseData>> callback) {
        mService.registerUser(new APIRequest<>(mHeader, requestData)).enqueue(callback);
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
