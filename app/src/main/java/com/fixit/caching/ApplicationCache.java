package com.fixit.caching;

import com.fixit.factories.APIFactory;

/**
 * Created by Kostyantin on 8/26/2017.
 */

public class ApplicationCache {

    private final TradesmanCache mTradesmanCache;

    public ApplicationCache(APIFactory apiFactory) {
        mTradesmanCache = new TradesmanCache(apiFactory.createDataServiceApi());
    }

    public TradesmanCache getTradesmanCache() {
        return mTradesmanCache;
    }
}
