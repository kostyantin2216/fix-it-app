package com.fixit.rest.apis;

import com.fixit.data.Profession;
import com.fixit.rest.queries.DataApiQuery;
import com.fixit.rest.queries.DataQueryCriteria;
import com.fixit.rest.services.ProfessionDataService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 12/23/2016.
 */

public class ProfessionDataAPI implements ServerDataAPI<Profession> {

    public final static String API_NAME = "data/Professions";

    private ProfessionDataService mService;

    public ProfessionDataAPI(ProfessionDataService service) {
        mService = service;
    }

    @Override
    public Call<Profession> find(Integer id) {
        return mService.find(id);
    }

    @Override
    public Call<List<Profession>> findAll() {
        return mService.findAll();
    }

    @Override
    public Call<Profession> create(Profession obj) {
        return mService.create(obj);
    }

    @Override
    public Call<Profession> update(Profession obj) {
        return mService.update(obj);
    }

    @Override
    public Call<Profession> delete(Integer id) {
        return mService.delete(id);
    }

    @Override
    public Call<List<Profession>> query(DataQueryCriteria criteria) {
        return mService.query(criteria.build());
    }

    @Override
    public Call<List<Profession>> query(DataApiQuery query) {
        return mService.query(new DataApiQuery[] {query});
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
