package com.fixit.core.rest.services;

import com.fixit.core.data.ServerLog;
import com.fixit.core.rest.apis.ServerLogDataAPI;
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
 * Created by Kostyantin on 12/20/2016.
 */

public interface ServerLogDataService {

    @GET(ServerLogDataAPI.API_NAME + "/{id}")
    Call<ServerLog> find(@Path("id") Integer id);

    @GET(ServerLogDataAPI.API_NAME)
    Call<List<ServerLog>> findAll();

    @POST(ServerLogDataAPI.API_NAME)
    Call<ServerLog> create(@Body ServerLog signal);

    @PUT(ServerLogDataAPI.API_NAME)
    Call<ServerLog> update(@Body ServerLog signal);

    @DELETE(ServerLogDataAPI.API_NAME + "/{id}")
    Call<ServerLog> delete(@Path("id") Integer id);

    @POST(ServerLogDataAPI.API_NAME + "/query")
    Call<List<ServerLog>> query(@Body DataQueryRequest req);

}
