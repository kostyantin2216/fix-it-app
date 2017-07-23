package com.fixit.core.factories;

import android.content.Context;

import com.fixit.core.config.AppConfig;
import com.fixit.core.rest.apis.AppInstallationDataAPI;
import com.fixit.core.rest.apis.DataServiceAPI;
import com.fixit.core.rest.apis.JobReasonDataAPI;
import com.fixit.core.rest.apis.MapAreaDataAPI;
import com.fixit.core.rest.apis.OrderServiceAPI;
import com.fixit.core.rest.apis.ProfessionDataAPI;
import com.fixit.core.rest.apis.ReviewDataAPI;
import com.fixit.core.rest.apis.SearchServiceAPI;
import com.fixit.core.rest.apis.ServerLogDataAPI;
import com.fixit.core.rest.apis.SynchronizationServiceAPI;
import com.fixit.core.rest.apis.UserServiceAPI;
import com.fixit.core.rest.apis.twilio.TwilioAPI;
import com.fixit.core.rest.requests.APIRequestHeader;
import com.fixit.core.rest.services.AppInstallationDataService;
import com.fixit.core.rest.services.DataService;
import com.fixit.core.rest.services.JobReasonDataService;
import com.fixit.core.rest.services.MapAreaDataService;
import com.fixit.core.rest.services.OrderService;
import com.fixit.core.rest.services.ProfessionDataService;
import com.fixit.core.rest.services.ReviewDataService;
import com.fixit.core.rest.services.SearchService;
import com.fixit.core.rest.services.ServerLogDataService;
import com.fixit.core.rest.services.SynchronizationService;
import com.fixit.core.rest.services.TwilioExternalService;
import com.fixit.core.rest.services.UserService;
import com.fixit.core.utils.GlobalPreferences;

import retrofit2.Retrofit;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public class APIFactory {

    private final Retrofit mClient;
    private final APIRequestHeader mHeader;

    public APIFactory(Context context) {
        String baseUrl = AppConfig.getString(context, AppConfig.KEY_SERVER_API_BASE_URL, "");
        mClient = RetrofitFactory.createServerRetrofitClient(context, baseUrl);
        mHeader = new APIRequestHeader();
        GlobalPreferences.fillApiRequestHeader(context, mHeader);
    }

    public void updateLatestScreen(String latestScreen) {
        mHeader.setLatestScreen(latestScreen);
    }

    public void updateAppInstallationId(String installationId) {
        mHeader.setInstallationId(installationId);
    }

    public void updateUserId(String userId) {
        mHeader.setUserId(userId);
    }

    // DATA APIS

    public AppInstallationDataAPI createAppInstallationApi() {
        return new AppInstallationDataAPI(mClient.create(AppInstallationDataService.class));
    }

    public JobReasonDataAPI createJobReasonApi() {
        return new JobReasonDataAPI(mClient.create(JobReasonDataService.class));
    }

    public MapAreaDataAPI createMapAreaApi() {
        return new MapAreaDataAPI(mClient.create(MapAreaDataService.class));
    }

    public ProfessionDataAPI createProfessionApi() {
        return new ProfessionDataAPI(mClient.create(ProfessionDataService.class));
    }

    public ReviewDataAPI createReviewApi() {
        return new ReviewDataAPI(mClient.create(ReviewDataService.class));
    }

    public ServerLogDataAPI createServerLogApi() {
        return new ServerLogDataAPI(mClient.create(ServerLogDataService.class));
    }

    // SERVICE APIS

    public DataServiceAPI createDataServiceApi() {
        return new DataServiceAPI(mHeader, mClient.create(DataService.class));
    }

    public OrderServiceAPI createOrderServceApi() {
        return new OrderServiceAPI(mHeader, mClient.create(OrderService.class));
    }

    public SearchServiceAPI createSearchServiceApi() {
        return new SearchServiceAPI(mHeader, mClient.create(SearchService.class));
    }

    public SynchronizationServiceAPI createSynchronizationApi() {
        return new SynchronizationServiceAPI(mHeader, mClient.create(SynchronizationService.class));
    }

    public UserServiceAPI createUserServiceApi() {
        return new UserServiceAPI(mHeader, mClient.create(UserService.class));
    }

    // EXTERNAL APIS

    /**
     * Currently only used for telephone number verification upon user registration, so no need for
     * early or lazy initialization of the retrofit client as a class variable, just create on demand.
     */
    public TwilioAPI createTwilioApi(Context context) {
        String baseUrl = AppConfig.getString(context, AppConfig.KEY_TWILIO_BASE_URL, "");
        String accSid = AppConfig.getString(context, AppConfig.KEY_TWILIO_ACCOUNT_SID, "");
        String authToken = AppConfig.getString(context, AppConfig.KEY_TWILIO_AUTH_TOKEN, "");

        Retrofit retrofit = RetrofitFactory.createRetrofitClient(context, baseUrl, accSid, authToken);
        return new TwilioAPI(retrofit.create(TwilioExternalService.class), accSid);
    }

}
