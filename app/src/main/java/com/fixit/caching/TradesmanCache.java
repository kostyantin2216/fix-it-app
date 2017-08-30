package com.fixit.caching;

import android.content.Context;
import android.util.SparseArray;

import com.fixit.data.Tradesman;
import com.fixit.rest.apis.DataServiceAPI;
import com.fixit.rest.callbacks.ManagedServiceCallback;
import com.fixit.rest.responses.data.TradesmenResponseData;

/**
 * Created by Kostyantin on 8/26/2017.
 */

public class TradesmanCache {

    private final SparseArray<Tradesman> mTradesmen = new SparseArray<>();
    private final DataServiceAPI mApi;

    public TradesmanCache(DataServiceAPI dataServiceApi) {
        mApi = dataServiceApi;
    }

    public void put(Tradesman... tradesmen) {
        for(Tradesman tradesman : tradesmen) {
            mTradesmen.put(tradesman.get_id().hashCode(), tradesman);
        }
    }

    public void get(Context context, final CacheCallback<Tradesman> cacheCallback, String... tradesmenIds) {
        final Tradesman[] results = new Tradesman[tradesmenIds.length];
        final SparseArray<String> missingTradesmen = new SparseArray<>();

        for(int i = 0; i < tradesmenIds.length; i++) {
            Tradesman tradesman = mTradesmen.get(tradesmenIds[i].hashCode());
            if(tradesman != null) {
                results[i] = tradesman;
            } else {
                missingTradesmen.put(i, tradesmenIds[i]);
            }
        }

        final int missingTradesmenCount = missingTradesmen.size();
        if(missingTradesmenCount == 0) {
            cacheCallback.onResult(results);
        } else {
            String[] missingTradesmenIds = new String[missingTradesmenCount];
            for(int i = 0; i < missingTradesmenCount; i++) {
                missingTradesmenIds[i] = missingTradesmen.valueAt(i);
            }

            mApi.getTradesmen(missingTradesmenIds, new ManagedServiceCallback<TradesmenResponseData>(context, cacheCallback) {
                @Override
                public void onResponse(TradesmenResponseData responseData) {
                    for(Tradesman tradesman : responseData.getTradesmen()) {
                        int missingIndex = missingTradesmen.indexOfValue(tradesman.get_id());
                        int resultIndex = missingTradesmen.keyAt(missingIndex);
                        results[resultIndex] = tradesman;
                    }
                    cacheCallback.onResult(results);
                }
            });
        }
    }

}
