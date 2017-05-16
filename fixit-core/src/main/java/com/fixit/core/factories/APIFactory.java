package com.fixit.core.factories;

import android.content.Context;

import com.fixit.core.config.AppConfig;
import com.fixit.core.rest.apis.AppInstallationAPI;
import com.fixit.core.rest.apis.DataServiceAPI;
import com.fixit.core.rest.apis.MapAreaDataAPI;
import com.fixit.core.rest.apis.ProfessionDataAPI;
import com.fixit.core.rest.apis.SearchServiceAPI;
import com.fixit.core.rest.apis.ServerLogDataAPI;
import com.fixit.core.rest.apis.SynchronizationServiceAPI;
import com.fixit.core.rest.apis.twilio.TwilioAPI;
import com.fixit.core.rest.apis.UserServiceAPI;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.services.AppInstallationService;
import com.fixit.core.rest.services.DataServiceService;
import com.fixit.core.rest.services.MapAreaService;
import com.fixit.core.rest.services.ProfessionService;
import com.fixit.core.rest.services.SearchServiceService;
import com.fixit.core.rest.services.ServerLogService;
import com.fixit.core.rest.services.SynchronizationServiceService;
import com.fixit.core.rest.services.TwilioService;
import com.fixit.core.rest.services.UserServiceService;
import com.fixit.core.utils.PrefUtils;

import retrofit2.Retrofit;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public class APIFactory {

    private final static String LOG_TAG = "#" + APIFactory.class.getSimpleName();

    private final Retrofit mClient;
    private final APIRequestHeader mHeader;

    public APIFactory(Context context) {
        String baseUrl = AppConfig.getString(context, AppConfig.KEY_SERVER_API_BASE_URL, "");
        mClient = RetrofitFactory.createServerRetrofitClient(context, baseUrl);
        mHeader = new APIRequestHeader();
        PrefUtils.fillApiRequestHeader(context, mHeader);
    }

    public enum API {
        APP_INSTALLATION,
        DATA_SERVICE,
        MAP_AREA,
        PROFESSION,
        SEARCH_SERVICE,
        SERVER_LOG,
        SYNCHRONIZATION_SERVICE,
        USER_SERVICE
    }

    public AppInstallationAPI createAppInstallationApi() {
        return new AppInstallationAPI(mClient.create(AppInstallationService.class));
    }

    public DataServiceAPI createDataServiceApi() {
        return new DataServiceAPI(mHeader, mClient.create(DataServiceService.class));
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

    public UserServiceAPI createUserServiceApi() {
        return new UserServiceAPI(mHeader, mClient.create(UserServiceService.class));
    }

    /**
     * Currently only used for telephone number verification upon user registration, so no need for
     * early or lazy initialization of the retrofit client as a class variable, just create on demand.
     */
    public TwilioAPI createTwilioApi(Context context) {
        String baseUrl = AppConfig.getString(context, AppConfig.KEY_TWILIO_BASE_URL, "");
        String accSid = AppConfig.getString(context, AppConfig.KEY_TWILIO_ACCOUNT_SID, "");
        String authToken = AppConfig.getString(context, AppConfig.KEY_TWILIO_AUTH_TOKEN, "");

        Retrofit retrofit = RetrofitFactory.createRetrofitClient(context, baseUrl, accSid, authToken);
        return new TwilioAPI(retrofit.create(TwilioService.class), accSid);
    }

}
