package com.fixit.rest.services;

import com.fixit.data.AppInstallation;
import com.fixit.rest.apis.AppInstallationDataAPI;
import com.fixit.rest.queries.DataApiQuery;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by konstantin on 4/3/2017.
 */

public interface AppInstallationDataService {

    @GET(AppInstallationDataAPI.API_NAME + "/{id}")
    Call<AppInstallation> find(@Path("id") Integer id);

    @GET(AppInstallationDataAPI.API_NAME)
    Call<List<AppInstallation>> findAll();

    @POST(AppInstallationDataAPI.API_NAME)
    Call<AppInstallation> create(@Body AppInstallation signal);

    @PUT(AppInstallationDataAPI.API_NAME)
    Call<AppInstallation> update(@Body AppInstallation signal);

    @DELETE(AppInstallationDataAPI.API_NAME + "/{id}")
    Call<AppInstallation> delete(@Path("id") Integer id);

    @POST(AppInstallationDataAPI.API_NAME + "/query")
    Call<List<AppInstallation>> query(@Body DataApiQuery[] queries);

}
