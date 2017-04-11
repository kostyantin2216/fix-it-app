package com.fixit.core.rest.services;

import com.fixit.core.data.AppInstallation;
import com.fixit.core.data.MapArea;
import com.fixit.core.rest.apis.AppInstallationAPI;
import com.fixit.core.rest.apis.MapAreaDataAPI;

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

public interface AppInstallationService {

    @GET(AppInstallationAPI.API_NAME + "/{id}")
    Call<AppInstallation> find(@Path("id") Integer id);

    @GET(AppInstallationAPI.API_NAME)
    Call<List<AppInstallation>> findAll();

    @POST(AppInstallationAPI.API_NAME)
    Call<AppInstallation> create(@Body AppInstallation signal);

    @PUT(AppInstallationAPI.API_NAME)
    Call<AppInstallation> update(@Body AppInstallation signal);

    @DELETE(AppInstallationAPI.API_NAME + "/{id}")
    Call<AppInstallation> delete(@Path("id") Integer id);

}
