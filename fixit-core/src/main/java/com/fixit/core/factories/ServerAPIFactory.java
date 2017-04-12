package com.fixit.core.factories;

import android.content.Context;

import com.fixit.core.config.AppConfig;
import com.fixit.core.rest.ServerDataAPI;
import com.fixit.core.rest.apis.AppInstallationAPI;
import com.fixit.core.rest.apis.MapAreaDataAPI;
import com.fixit.core.rest.apis.ProfessionDataAPI;
import com.fixit.core.rest.apis.SearchServiceAPI;
import com.fixit.core.rest.apis.ServerLogDataAPI;
import com.fixit.core.rest.apis.SynchronizationServiceAPI;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.services.AppInstallationService;
import com.fixit.core.rest.services.MapAreaService;
import com.fixit.core.rest.services.ProfessionService;
import com.fixit.core.rest.services.SearchServiceService;
import com.fixit.core.rest.services.ServerLogService;
import com.fixit.core.rest.services.SynchronizationServiceService;
import com.fixit.core.utils.FILog;
import com.fixit.core.utils.PrefUtils;

import retrofit2.Retrofit;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public class ServerAPIFactory {

    private final static String LOG_TAG = "#" + ServerAPIFactory.class.getSimpleName();

    private final Retrofit mClient;
    private final APIRequestHeader mHeader;

    public ServerAPIFactory(Context context) {
        String baseUrl = AppConfig.getString(context, AppConfig.KEY_SERVER_API_BASE_URL, "");
        mClient = RetrofitFactory.createRetrofitClient(context, baseUrl);
        mHeader = new APIRequestHeader();
        PrefUtils.fillApiRequestHeader(context, mHeader);
    }

    public enum API {
        APP_INSTALLATION,
        MAP_AREA,
        PROFESSION,
        SEARCH_SERVICE,
        SERVER_LOG,
        SYNCHRONIZATION_SERVICE
    }

    @SuppressWarnings("unchecked")
    public <T extends ServerDataAPI> T createApi(API api) {
        FILog.i(LOG_TAG, "creating api " + api.name());
        switch (api) {
            case APP_INSTALLATION:
                return (T) new AppInstallationAPI(mClient.create(AppInstallationService.class));
            case MAP_AREA:
                return (T) new MapAreaDataAPI(mClient.create(MapAreaService.class));
            case PROFESSION:
                return (T) new ProfessionDataAPI(mClient.create(ProfessionService.class));
            case SEARCH_SERVICE:
                return (T) new SearchServiceAPI(mHeader, mClient.create(SearchServiceService.class));
            case SERVER_LOG:
                return (T) new ServerLogDataAPI(mClient.create(ServerLogService.class));
            case SYNCHRONIZATION_SERVICE:
                return (T) new SynchronizationServiceAPI(mHeader, mClient.create(SynchronizationServiceService.class));
            default:
                return null;
        }
    }

    public AppInstallationAPI createAppInstallationApi() {
        return new AppInstallationAPI(mClient.create(AppInstallationService.class));
    }

    public MapAreaDataAPI createMapAreaApi() {
        return new MapAreaDataAPI(mClient.create(MapAreaService.class));
    }

    public ProfessionDataAPI createProfessionApi() {
        return new ProfessionDataAPI(mClient.create(ProfessionService.class));
    }

    public SearchServiceAPI createSearchServiceApi() {
        return new SearchServiceAPI(mHeader, mClient.create(SearchServiceService.class));
    }

    public ServerLogDataAPI createServerLogApi() {
        return new ServerLogDataAPI(mClient.create(ServerLogService.class));
    }

    public SynchronizationServiceAPI createSynchronizationApi() {
        return new SynchronizationServiceAPI(mHeader, mClient.create(SynchronizationServiceService.class));
    }

}
