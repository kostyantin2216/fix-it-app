package com.fixit.caching;

import android.content.Context;
import android.util.SparseArray;

import com.fixit.data.Tradesman;
import com.fixit.rest.apis.DataServiceAPI;
import com.fixit.rest.callbacks.ManagedServiceCallback;
import com.fixit.rest.responses.data.TradesmenResponseData;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;

import java.util.HashMap;
import java.util.Map;

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
        final HashMap<String, Integer> missingTradesmen = new HashMap<>();

        FILog.i(Constants.LOG_TAG_TRADESMAN_CACHE, "getting " + tradesmenIds.length + " tradesmen");

        for(int i = 0; i < tradesmenIds.length; i++) {
            Tradesman tradesman = mTradesmen.get(tradesmenIds[i].hashCode());
            if(tradesman != null) {
                results[i] = tradesman;
            } else {
                missingTradesmen.put(tradesmenIds[i], i);
            }
        }

        final int missingTradesmenCount = missingTradesmen.size();
        if(missingTradesmenCount == 0) {
            FILog.i(Constants.LOG_TAG_TRADESMAN_CACHE, "perfect cache hit!");

            cacheCallback.onResult(results);
        } else {
            FILog.i(Constants.LOG_TAG_TRADESMAN_CACHE, "fetching " + missingTradesmenCount + " missing tradesmen");

            String[] missingTradesmenIds = new String[missingTradesmenCount];
            int index = 0;
            for(Map.Entry<String, Integer> entry : missingTradesmen.entrySet()) {
                missingTradesmenIds[index++] = entry.getKey();
            }

            mApi.getTradesmen(missingTradesmenIds, new ManagedServiceCallback<TradesmenResponseData>(context, cacheCallback) {
                @Override
                public void onResponse(TradesmenResponseData responseData) {
                    for(Tradesman tradesman : responseData.getTradesmen()) {
                        Integer resultIndex = missingTradesmen.get(tradesman.get_id());
                        results[resultIndex] = tradesman;
                    }
                    cacheCallback.onResult(results);
                }
            });
        }
    }

}
