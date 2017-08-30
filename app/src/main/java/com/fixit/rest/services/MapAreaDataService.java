package com.fixit.rest.services;

import com.fixit.data.MapArea;
import com.fixit.rest.apis.MapAreaDataAPI;
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
 * Created by Kostyantin on 12/20/2016.
 */

public interface MapAreaDataService {

    @GET(MapAreaDataAPI.API_NAME + "/{id}")
    Call<MapArea> find(@Path("id") Integer id);

    @GET(MapAreaDataAPI.API_NAME)
    Call<List<MapArea>> findAll();

    @POST(MapAreaDataAPI.API_NAME)
    Call<MapArea> create(@Body MapArea signal);

    @PUT(MapAreaDataAPI.API_NAME)
    Call<MapArea> update(@Body MapArea signal);

    @DELETE(MapAreaDataAPI.API_NAME + "/{id}")
    Call<MapArea> delete(@Path("id") Integer id);

    @POST(MapAreaDataAPI.API_NAME + "/query")
    Call<List<MapArea>> query(@Body DataApiQuery[] queries);

}
