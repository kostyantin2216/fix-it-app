package com.fixit.core.rest.apis;

import com.fixit.core.rest.requests.APIRequest;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.requests.data.SynchronizationRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.data.SynchronizationResponseData;
import com.fixit.core.rest.services.SynchronizationService;

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
