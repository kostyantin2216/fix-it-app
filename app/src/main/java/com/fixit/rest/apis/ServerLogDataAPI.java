package com.fixit.rest.apis;

import com.fixit.data.ServerLog;
import com.fixit.rest.queries.DataApiQuery;
import com.fixit.rest.queries.DataQueryCriteria;
import com.fixit.rest.services.ServerLogDataService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 12/24/2016.
 */

public class ServerLogDataAPI implements ServerDataAPI<ServerLog> {

    public final static String API_NAME = "data/AppLogs";

    private final ServerLogDataService mService;

    public ServerLogDataAPI(ServerLogDataService service) {
        mService = service;
    }

    @Override
    public Call<ServerLog> find(Integer id) {
        return mService.find(id);
    }

    @Override
    public Call<List<ServerLog>> findAll() {
        return mService.findAll();
    }

    @Override
    public Call<ServerLog> create(ServerLog obj) {
        return mService.create(obj);
    }

    @Override
    public Call<ServerLog> update(ServerLog obj) {
        return mService.update(obj);
    }

    @Override
    public Call<ServerLog> delete(Integer id) {
        return mService.delete(id);
    }

    @Override
    public Call<List<ServerLog>> query(DataQueryCriteria criteria) {
        return mService.query(criteria.build());
    }

    @Override
    public Call<List<ServerLog>> query(DataApiQuery query) {
        return mService.query(new DataApiQuery[] {query});
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
