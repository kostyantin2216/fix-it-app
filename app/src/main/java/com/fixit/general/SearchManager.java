package com.fixit.general;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fixit.config.AppConfig;
import com.fixit.data.JobLocation;
import com.fixit.data.MutableLatLng;
import com.fixit.data.Profession;
import com.fixit.data.Tradesman;
import com.fixit.rest.APIError;
import com.fixit.rest.apis.SearchServiceAPI;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.rest.callbacks.ManagedServiceCallback;
import com.fixit.rest.requests.data.SearchRequestData;
import com.fixit.rest.requests.data.SearchResultRequestData;
import com.fixit.rest.responses.APIResponse;
import com.fixit.rest.responses.APIResponseHeader;
import com.fixit.rest.responses.data.SearchResponseData;
import com.fixit.rest.responses.data.SearchResultResponseData;

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

    public void sendSearch(Context context, final Profession profession, JobLocation location, final SearchCallback searchCallback) {
        double lat = location.getLat();
        double lng = location.getLng();
        SearchRequestData requestData = new SearchRequestData(profession.getId(), new MutableLatLng(lat, lng));
        mApi.beginSearch(requestData, new ManagedServiceCallback<SearchResponseData>(context, searchCallback) {
            @Override
            public void onResponse(SearchResponseData responseData) {
                searchCallback.onSearchStarted(profession, location, responseData.getSearchKey());
            }

            @Override
            public void onAppServiceError(List<APIError> errors) {
                if (APIError.contains(APIError.Error.UNSUPPORTED, errors)){
                    searchCallback.unsupportedAddress();
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
        void unsupportedAddress();
        void onSearchStarted(Profession profession, JobLocation location, String searchId);
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
