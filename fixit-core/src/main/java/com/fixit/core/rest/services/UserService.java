package com.fixit.core.rest.services;

import com.fixit.core.rest.apis.UserServiceAPI;
import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.data.UserRegistrationRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.UserRegistrationResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by konstantin on 5/15/2017.
 */

public interface UserService {

   @POST(UserServiceAPI.API_NAME + "/registerUser")
    Call<APIResponse<UserRegistrationResponseData>> registerUser(@Body APIRequest<UserRegistrationRequestData> request);

}
