package com.fixit.general;

import android.content.Context;

import com.fixit.config.AppConfig;
import com.fixit.data.ServerLog;
import com.fixit.factories.RetrofitFactory;
import com.fixit.rest.apis.ServerLogDataAPI;
import com.fixit.rest.callbacks.EmptyCallback;
import com.fixit.rest.services.ServerLogDataService;
import com.fixit.utils.GlobalPreferences;

import java.util.Date;

import retrofit2.Retrofit;

/**
 * Created by Kostyantin on 4/12/2017.
 */

public class ErrorReporter {

    private static ServerLogDataAPI mServerApi;

    public static void report(Context context, String level, String tag, String msg, String stackTrace) {
        if(mServerApi == null) {
            String baseUrl = AppConfig.getString(context, AppConfig.KEY_SERVER_API_BASE_URL, "");
            Retrofit retrofit = RetrofitFactory.createServerRetrofitClient(context, baseUrl);
            mServerApi = new ServerLogDataAPI(retrofit.create(ServerLogDataService.class));
        }

        ServerLog log = new ServerLog(
                level,
                tag,
                msg,
                stackTrace,
                AppConfig.getDeviceInfo(context),
                AppConfig.getVersionInfo(context),
                new Date()
        );

        GlobalPreferences.fillServerLog(context, log);

        mServerApi.create(log).enqueue(new EmptyCallback<ServerLog>());
    }

}
