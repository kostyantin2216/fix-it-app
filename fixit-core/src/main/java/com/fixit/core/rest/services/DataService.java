package com.fixit.core.rest.services;

import com.fixit.core.rest.apis.DataServiceAPI;
import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.data.TelephoneVerificationRequestData;
import com.fixit.core.rest.requests.data.TradesmanReviewsRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.TelephoneVerificationResponseData;
import com.fixit.core.rest.responses.data.TradesmanReviewResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by konstantin on 5/7/2017.
 */

public interface DataService {

    @POST(DataServiceAPI.API_NAME + "/reviewsForTradesman")
    Call<APIResponse<TradesmanReviewResponseData>> getReviewsForTradesman(@Body APIRequest<TradesmanReviewsRequestData> request);

}
