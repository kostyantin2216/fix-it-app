package com.fixit.core.rest.apis;

import com.fixit.core.data.JobReason;
import com.fixit.core.rest.queries.DataApiQuery;
import com.fixit.core.rest.queries.DataQueryCriteria;
import com.fixit.core.rest.services.JobReasonDataService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class JobReasonDataAPI implements ServerDataAPI<JobReason> {

    public final static String API_NAME = "data/JobReasons";

    private final JobReasonDataService mService;

    public JobReasonDataAPI(JobReasonDataService dataService) {
        mService = dataService;
    }

    @Override
    public Call<JobReason> find(Integer id) {
        return mService.find(id);
    }

    @Override
    public Call<List<JobReason>> findAll() {
        return mService.findAll();
    }

    @Override
    public Call<JobReason> create(JobReason obj) {
        return mService.create(obj);
    }

    @Override
    public Call<JobReason> update(JobReason obj) {
        return mService.update(obj);
    }

    @Override
    public Call<JobReason> delete(Integer id) {
        return mService.delete(id);
    }

    @Override
    public Call<List<JobReason>> query(DataQueryCriteria criteria) {
        return mService.query(criteria.build());
    }

    @Override
    public Call<List<JobReason>> query(DataApiQuery query) {
        return mService.query(new DataApiQuery[] {query});
    }

    public Call<List<JobReason>> findForProfession(int professionId) {;
        return mService.query(new DataApiQuery[] {
                new DataApiQuery("professionId", "=", String.valueOf(professionId))
        });
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
