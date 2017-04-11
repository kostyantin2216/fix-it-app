package com.fixit.core.general;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fixit.core.config.AppConfig;
import com.fixit.core.data.Profession;
import com.fixit.core.data.Tradesman;
import com.fixit.core.rest.APIError;
import com.fixit.core.rest.callbacks.AppServiceErrorCallback;
import com.fixit.core.rest.apis.AppServiceAPI;
import com.fixit.core.rest.callbacks.AppServiceCallback;
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

    private final AppServiceAPI mApi;

    public SearchManager(AppServiceAPI api) {
        this.mApi = api;
    }

    public void sendSearch(Context context, Profession profession, String address, final SearchCallback searchCallback) {
        SearchRequestData requestData = new SearchRequestData(profession.getId(), address);
        mApi.beginSearch(requestData, new AppServiceCallback<SearchResponseData>(context) {
            @Override
            public void onResponse(SearchResponseData responseData) {
                if(responseData.isAddressSupported()) {
                    searchCallback.onSearchStarted(responseData.getSearchId());
                } else {
                    searchCallback.invalidAddress();
                }
            }

            @Override
            public void onAppServiceError(List<APIError> errors) {
                searchCallback.onAppServiceError(errors);
            }

            @Override
            public void onRetryFailure(Call<APIResponse<SearchResponseData>> call, Throwable t) {
                searchCallback.onUnexpectedErrorOccurred(t.getMessage(), t);
            }
        });
    }

    public ResultsFetcher fetchResults(Context context, String searchId, ResultCallback resultCallback) {
        ResultsFetcher resultsFetcher = new ResultsFetcher(context, searchId, resultCallback);
        resultsFetcher.start();
        return resultsFetcher;
    }

    public interface SearchCallback extends UnexpectedErrorCallback, AppServiceErrorCallback {
        void invalidAddress();
        void onSearchStarted(String searchId);
    }

    public interface ResultCallback extends UnexpectedErrorCallback, AppServiceErrorCallback {
        void onResultsReceived(List<Tradesman> tradesmen, Map<String, Integer> reviewCountForTradesmen);
        void onResultsFetchTimeout();
    }

    public class ResultsFetcher extends Thread {

        private final String searchId;
        private final ResultCallback resultCallback;

        private final Handler handler;

        private final int pollingRetryLimit;
        private final int pollingRetryIntervalMs;
        private final int errorRetryLimit;
        private final int errorRetryIntervalMs;

        private int pollingRetries = 0;
        private int errorRetries = 0;

        private volatile boolean isCancelled = false;
        private boolean isFinished = false;

        private ResultsFetcher(Context context, String searchId, ResultCallback resultCallback) {
            this.searchId = searchId;
            this.resultCallback = resultCallback;
            this.handler = new Handler(Looper.getMainLooper());

            this.pollingRetryLimit = AppConfig.getInt(context, AppConfig.KEY_SEARCH_RESULT_POLLING_RETRY_LIMIT, 5);
            this.pollingRetryIntervalMs = AppConfig.getInt(context, AppConfig.KEY_SEARCH_RESULT_POLLING_RETRY_INTERVAL_MS, 800);
            this.errorRetryLimit = AppConfig.getInt(context, AppConfig.KEY_SERVER_CONNECTION_RETRY_LIMIT, 5);
            this.errorRetryIntervalMs = AppConfig.getInt(context, AppConfig.KEY_SERVER_CONNECTION_RETRY_INTERVAL_MS, 800);
        }

        @Override
        public void run() {
            SearchResultRequestData requestData = new SearchResultRequestData(searchId);
            execute(mApi.fetchResults(requestData));
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
