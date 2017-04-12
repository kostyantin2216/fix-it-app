package com.fixit.core.general;

import android.content.Context;

import com.fixit.core.config.AppConfig;
import com.fixit.core.data.ServerLog;
import com.fixit.core.factories.RetrofitFactory;
import com.fixit.core.rest.apis.ServerLogDataAPI;
import com.fixit.core.rest.callbacks.EmptyCallback;
import com.fixit.core.rest.services.ServerLogService;
import com.fixit.core.utils.PrefUtils;

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
            Retrofit retrofit = RetrofitFactory.createRetrofitClient(context, baseUrl);
            mServerApi = new ServerLogDataAPI(retrofit.create(ServerLogService.class));
        }

        ServerLog log = new ServerLog(
                PrefUtils.getUserId(context),
                PrefUtils.getInstallationId(context),
                level,
                tag,
                msg,
                stackTrace,
                AppConfig.getDeviceInfo(),
                AppConfig.getVersionInfo(context),
                new Date()
        );

        mServerApi.create(log).enqueue(new EmptyCallback<ServerLog>());
    }

}
