package com.fixit.core.general;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fixit.core.config.AppConfig;
import com.fixit.core.data.MutableLatLng;
import com.fixit.core.data.Profession;
import com.fixit.core.data.Tradesman;
import com.fixit.core.rest.APIError;
import com.fixit.core.rest.apis.SearchServiceAPI;
import com.fixit.core.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.core.rest.callbacks.ManagedServiceCallback;
import com.fixit.core.rest.requests.data.SearchRequestData;
import com.fixit.core.rest.requests.data.SearchResultRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.APIResponseHeader;
import com.fixit.core.rest.responses.data.SearchResponseData;
import com.fixit.core.rest.responses.data.SearchResultResponseData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by konstantin on 3/30/2017.
 */

public class SearchManager {

    private final SearchServiceAPI mApi;

    public SearchManager(SearchServiceAPI api) {
        this.mApi = api;
    }

    public void sendSearch(Context context, Profession profession, MutableLatLng location, final SearchCallback searchCallback) {
        SearchRequestData requestData = new SearchRequestData(profession.getId(), location);
        mApi.beginSearch(requestData, new ManagedServiceCallback<SearchResponseData>(context, searchCallback) {
            @Override
            public void onResponse(SearchResponseData responseData) {
                searchCallback.onSearchStarted(responseData.getSearchKey());
            }

            @Override
            public void onAppServiceError(List<APIError> errors) {
                if (APIError.contains(APIError.Error.UNSUPPORTED, errors)){
                    searchCallback.invalidAddress();
                } else {
                    super.onAppServiceError(errors);
                }
            }
        });
    }

    public ResultsFetcher fetchResults(Context context, String searchId, ResultCallback resultCallback) {
        ResultsFetcher resultsFetcher = new ResultsFetcher(context, searchId, resultCallback, mApi);
        resultsFetcher.start();
        return resultsFetcher;
    }

    public interface SearchCallback extends GeneralServiceErrorCallback {
        void invalidAddress();
        void onSearchStarted(String searchId);
    }

    public interface ResultCallback extends GeneralServiceErrorCallback {
        void onResultsReceived(List<Tradesman> tradesmen, Map<String, Integer> reviewCountForTradesmen);
        void onResultsFetchTimeout();
    }

    public static class ResultsFetcher extends Thread {

        private final String searchId;
        private final ResultCallback resultCallback;
        private final SearchServiceAPI searchApi;

        private final Handler handler;

        private final int pollingRetryLimit;
        private final int pollingRetryIntervalMs;
        private final int errorRetryLimit;
        private final int errorRetryIntervalMs;

        private int pollingRetries = 0;
        private int errorRetries = 0;

        private volatile boolean isCancelled = false;
        private boolean isFinished = false;

        private ResultsFetcher(Context context, String searchId, ResultCallback resultCallback, SearchServiceAPI searchApi) {
            this.searchId = searchId;
            this.searchApi = searchApi;
            this.resultCallback = resultCallback;
            this.handler = new Handler(Looper.getMainLooper());

            this.pollingRetryLimit = AppConfig.getInteger(context, AppConfig.KEY_SEARCH_RESULT_POLLING_RETRY_LIMIT, 5);
            this.pollingRetryIntervalMs = AppConfig.getInteger(context, AppConfig.KEY_SEARCH_RESULT_POLLING_RETRY_INTERVAL_MS, 800);
            this.errorRetryLimit = AppConfig.getInteger(context, AppConfig.KEY_SERVER_CONNECTION_RETRY_LIMIT, 5);
            this.errorRetryIntervalMs = AppConfig.getInteger(context, AppConfig.KEY_SERVER_CONNECTION_RETRY_INTERVAL_MS, 800);
        }

        @Override
        public void run() {
            SearchResultRequestData requestData = new SearchResultRequestData(searchId);
            execute(searchApi.fetchResults(requestData));
            isFinished = true;
        }

        private SearchResultResponseData execute(Call<APIResponse<SearchResultResponseData>> call) {
            if(call != null && !isCancelled) {
                try {
                    APIResponse<SearchResultResponseData> response = call.execute().body();

                    if(response != null) {
                        final APIResponseHeader header = response.getHeader();
                        final SearchResultResponseData data = response.getData();

                        if(header.hasErrors()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    resultCallback.onAppServiceError(header.getErrors());
                                }
                            });
                        } else if(data.isComplete()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    resultCallback.onResultsReceived(data.getTradesmen(), data.getReviewCountForTradesmen());
                                }
                            });
                        } else {
                            poll(call);
                        }

                    } else {
                        poll(call);
                    }

                } catch (IOException e) {
                    handleError(call, e);
                }
            }
            return null;
        }

        private SearchResultResponseData poll(Call<APIResponse<SearchResultResponseData>> call) {
            if(pollingRetries < pollingRetryLimit) {
                pollingRetries++;

                if(!isCancelled) {
                    try {
                        synchronized (this) {
                            wait(pollingRetryIntervalMs);
                        }
                    } catch (InterruptedException e1) {
                        // do nothing, continue with the flow.
                    }
                }

                return execute(call.clone());
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultCallback.onResultsFetchTimeout();
                    }
                });
            }
            return null;
        }

        private SearchResultResponseData handleError(Call<APIResponse<SearchResultResponseData>> call, final Throwable t) {
            if(errorRetries < errorRetryLimit) {
                errorRetries++;

                if(!isCancelled) {
                    try {
                        synchronized (this) {
                            wait(errorRetryIntervalMs);
                        }
                    } catch (InterruptedException e1) {
                        // do nothing, continue with the flow.
                    }
                }

                return execute(call.clone());
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultCallback.onUnexpectedErrorOccurred(t.getMessage(), t);
                    }
                });
            }
            return null;
        }

        public void cancel() {
            isCancelled = true;
        }

        public boolean isCancelled() {
            return isCancelled;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }

}
