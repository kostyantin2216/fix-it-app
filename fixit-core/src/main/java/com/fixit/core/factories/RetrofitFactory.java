package com.fixit.core.factories;

import android.content.Context;

import com.fixit.core.config.AppConfig;
import com.fixit.core.rest.adapters.SynchronizationResultDeserializer;
import com.fixit.core.synchronization.SynchronizationResult;
import com.fixit.core.utils.DateUtils;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
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

    public static Retrofit createRetrofitClient(Context context, String baseUrl, String user, String password) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createGeneralHttpClient(context, user, password))
                .addConverterFactory(createGeneralGsonConverterFactory())
                .build();
    }

    public static Retrofit createServerRetrofitClient(Context context, String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(createServerHttpClient(context))
                .addConverterFactory(createServerGsonConverterFactory())
                .build();
    }

    public static GsonConverterFactory createGeneralGsonConverterFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .setDateFormat(DateUtils.FORMAT_RFC_2822)
                        .create()
        );
    }

    private static GsonConverterFactory createServerGsonConverterFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .setDateFormat(DateUtils.FORMAT_REST_DATE)
                        .registerTypeAdapter(SynchronizationResult.class, new SynchronizationResultDeserializer())
                        .create()
        );
    }

    private static OkHttpClient createGeneralHttpClient(Context context, String user, String password) {
        return initHttpClientBuilder(context)
                .addInterceptor(new BasicAuthorizationInterceptor(user, password))
                .build();
    }

    private static OkHttpClient createServerHttpClient(Context context) {
        String apiKey = AppConfig.getString(context, AppConfig.KEY_API_KEY, "");
        String userAgent = AppConfig.getString(context, AppConfig.KEY_USER_AGENT, "");

        return initHttpClientBuilder(context)
                .addInterceptor(new ServerAuthorizationInterceptor(userAgent, apiKey))
                .build();
    }

    private static OkHttpClient.Builder initHttpClientBuilder(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
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
                );

        if(!AppConfig.isProduction(context)) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        return builder;
    }

    private static class BasicAuthorizationInterceptor implements Interceptor {

        private final String credentials;

        public BasicAuthorizationInterceptor(String userName, String password) {
            this.credentials = Credentials.basic(userName, password);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", credentials);
            requestBuilder.method(original.method(),original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }

    private static class ServerAuthorizationInterceptor implements Interceptor {

        private final String userAgent;
        private final String apiKey;

        public ServerAuthorizationInterceptor(String userAgent, String apiKey) {
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
