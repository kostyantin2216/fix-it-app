package com.fixit.rest.apis;

import com.fixit.rest.requests.APIRequest;
import com.fixit.rest.requests.APIRequestHeader;
import com.fixit.rest.requests.data.SynchronizationRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.data.SynchronizationResponseData;
import com.fixit.rest.services.SynchronizationService;

import retrofit2.Call;

/**
 * Created by Kostyantin on 4/12/2017.
 */

public class SynchronizationServiceAPI extends BaseServiceAPI {

    public final static String API_NAME = "services/synchronization";

    private final SynchronizationService mService;

    public SynchronizationServiceAPI(APIRequestHeader header, SynchronizationService service) {
        super(header);
        mService = service;
    }

    public Call<APIResponse<SynchronizationResponseData>> synchronize(SynchronizationRequestData data) {
        return mService.synchronize(new APIRequest<>(mHeader, data));
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }

}
