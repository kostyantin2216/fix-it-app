package com.fixit.core.rest.apis;

import com.fixit.core.rest.requests.APIRequestHeader;

/**
 * Created by Kostyantin on 4/12/2017.
 */

public abstract class BaseServiceAPI implements ServerAPI {

    protected final APIRequestHeader mHeader;

    public BaseServiceAPI(APIRequestHeader header) {
        this.mHeader = header;
    }

}
