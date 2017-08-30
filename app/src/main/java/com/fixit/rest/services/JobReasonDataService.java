package com.fixit.rest.services;

import com.fixit.data.JobReason;
import com.fixit.rest.apis.JobReasonDataAPI;
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

public interface JobReasonDataService {

    @GET(JobReasonDataAPI.API_NAME + "/{id}")
    Call<JobReason> find(@Path("id") Integer id);

    @GET(JobReasonDataAPI.API_NAME)
    Call<List<JobReason>> findAll();

    @POST(JobReasonDataAPI.API_NAME)
    Call<JobReason> create(@Body JobReason reason);

    @PUT(JobReasonDataAPI.API_NAME)
    Call<JobReason> update(@Body JobReason reason);

    @DELETE(JobReasonDataAPI.API_NAME + "/{id}")
    Call<JobReason> delete(@Path("id") Integer id);

    @POST(JobReasonDataAPI.API_NAME + "/query")
    Call<List<JobReason>> query(@Body DataApiQuery[] queries);

}
