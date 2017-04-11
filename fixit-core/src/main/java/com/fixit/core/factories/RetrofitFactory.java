package com.fixit.core.factories;

import android.content.Context;

import com.fixit.core.config.AppConfig;
import com.fixit.core.rest.SynchronizationResultDeserializer;
import com.fixit.core.synchronization.SynchronizationResult;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.DateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kostyantin on 12/19/2016.
 */
public class RetrofitFactory {

    public static Retrofit createRetrofitClient(Context context, String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(createHttpClient(context))
                .addConverterFactory(createGsonConverterFactory())
                .build();
    }

    private static GsonConverterFactory createGsonConverterFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .setDateFormat(DateUtils.FORMAT_REST_DATE)
                        .registerTypeAdapter(SynchronizationResult.class, new SynchronizationResultDeserializer())
                        .create()
        );
    }

    private static OkHttpClient createHttpClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        String apiKey = AppConfig.getString(context, AppConfig.CONFIG_API_KEY, "");
        String userAgent = AppConfig.getString(context, AppConfig.CONFIG_USER_AGENT, "");

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AuthorizationInterceptor(userAgent, apiKey))
                .connectTimeout(
                        AppConfig.getInt(context, AppConfig.KEY_RETROFIT_C_TO, 30).longValue(),
                        TimeUnit.SECONDS
                )
                .readTimeout(
                        AppConfig.getInt(context, AppConfig.KEY_RETROFIT_R_TO, 30).longValue(),
                        TimeUnit.SECONDS
                )
                .writeTimeout(
                        AppConfig.getInt(context, AppConfig.KEY_RETROFIT_W_TO, 30).longValue(),
                        TimeUnit.SECONDS
                ).build();
    }

    private static class AuthorizationInterceptor implements Interceptor {

        private final String userAgent;
        private final String apiKey;

        public AuthorizationInterceptor(String userAgent, String apiKey) {
            this.userAgent = userAgent;
            this.apiKey = apiKey;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("User-Agent", userAgent)
                    .header("X-Authorization", apiKey)
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        }
    }


}
