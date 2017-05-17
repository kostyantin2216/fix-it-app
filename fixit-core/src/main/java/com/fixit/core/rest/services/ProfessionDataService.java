package com.fixit.core.rest.services;

import com.fixit.core.data.Profession;
import com.fixit.core.rest.apis.ProfessionDataAPI;
import com.fixit.core.rest.queries.DataQueryRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Kostyantin on 12/23/2016.
 */

public interface ProfessionDataService {

    @GET(ProfessionDataAPI.API_NAME + "/{id}")
    Call<Profession> find(@Path("id") Integer id);

    @GET(ProfessionDataAPI.API_NAME)
    Call<List<Profession>> findAll();

    @POST(ProfessionDataAPI.API_NAME)
    Call<Profession> create(@Body Profession signal);

    @PUT(ProfessionDataAPI.API_NAME)
    Call<Profession> update(@Body Profession signal);

    @DELETE(ProfessionDataAPI.API_NAME + "/{id}")
    Call<Profession> delete(@Path("id") Integer id);

    @POST(ProfessionDataAPI.API_NAME + "/query")
    Call<List<Profession>> query(@Body DataQueryRequest req);

}
