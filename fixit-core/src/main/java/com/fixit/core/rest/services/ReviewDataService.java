package com.fixit.core.rest.services;

import com.fixit.core.data.Review;
import com.fixit.core.rest.apis.ReviewDataAPI;
import com.fixit.core.rest.queries.DataApiQuery;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by konstantin on 5/17/2017.
 */

public interface ReviewDataService {

    @GET(ReviewDataAPI.API_NAME + "/{id}")
    Call<Review> find(@Path("id") Integer id);

    @GET(ReviewDataAPI.API_NAME)
    Call<List<Review>> findAll();

    @POST(ReviewDataAPI.API_NAME)
    Call<Review> create(@Body Review signal);

    @PUT(ReviewDataAPI.API_NAME)
    Call<Review> update(@Body Review signal);

    @DELETE(ReviewDataAPI.API_NAME + "/{id}")
    Call<Review> delete(@Path("id") Integer id);

    @POST(ReviewDataAPI.API_NAME + "/query")
    Call<List<Review>> query(@Body DataApiQuery[] queries);

}
