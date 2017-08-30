package com.fixit.rest.apis;

import com.fixit.data.Tradesman;
import com.fixit.rest.queries.DataApiQuery;
import com.fixit.rest.queries.DataQueryCriteria;
import com.fixit.rest.services.TradesmanDataService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 6/1/2017.
 */

public class TradesmanDataAPI implements ServerDataAPI<Tradesman> {

    public final static String API_NAME = "data/Tradesmen";

    private final TradesmanDataService mService;

    public TradesmanDataAPI(TradesmanDataService dataService) {
        mService = dataService;
    }

    @Override
    public Call<Tradesman> find(Integer id) {
        return mService.find(id);
    }

    @Override
    public Call<List<Tradesman>> findAll() {
        return mService.findAll();
    }

    @Override
    public Call<Tradesman> create(Tradesman obj) {
        return mService.create(obj);
    }

    @Override
    public Call<Tradesman> update(Tradesman obj) {
        return mService.update(obj);
    }

    @Override
    public Call<Tradesman> delete(Integer id) {
        return mService.delete(id);
    }

    @Override
    public Call<List<Tradesman>> query(DataQueryCriteria criteria) {
        return mService.query(criteria.build());
    }

    @Override
    public Call<List<Tradesman>> query(DataApiQuery query) {
        return mService.query(new DataApiQuery[] {query});
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
