package com.fixit.rest.services;

import com.fixit.data.Tradesman;
import com.fixit.rest.apis.TradesmanDataAPI;
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
 * Created by Kostyantin on 6/1/2017.
 */

public interface TradesmanDataService {

    @GET(TradesmanDataAPI.API_NAME + "/{id}")
    Call<Tradesman> find(@Path("id") Integer id);

    @GET(TradesmanDataAPI.API_NAME)
    Call<List<Tradesman>> findAll();

    @POST(TradesmanDataAPI.API_NAME)
    Call<Tradesman> create(@Body Tradesman reason);

    @PUT(TradesmanDataAPI.API_NAME)
    Call<Tradesman> update(@Body Tradesman reason);

    @DELETE(TradesmanDataAPI.API_NAME + "/{id}")
    Call<Tradesman> delete(@Path("id") Integer id);

    @POST(TradesmanDataAPI.API_NAME + "/query")
    Call<List<Tradesman>> query(@Body DataApiQuery[] queries);

}
