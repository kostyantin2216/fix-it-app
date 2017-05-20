package com.fixit.core.rest.apis;

import com.fixit.core.data.MapArea;
import com.fixit.core.rest.queries.DataApiQuery;
import com.fixit.core.rest.queries.DataQueryCriteria;
import com.fixit.core.rest.services.MapAreaDataService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public class MapAreaDataAPI implements ServerDataAPI<MapArea> {

    public final static String API_NAME = "data/MapAreas";

    private final MapAreaDataService mService;

    public MapAreaDataAPI(MapAreaDataService service) {
        this.mService = service;
    }

    @Override
    public Call<MapArea> find(Integer id) {
        return mService.find(id);
    }

    @Override
    public Call<List<MapArea>> findAll() {
        return mService.findAll();
    }

    @Override
    public Call<MapArea> create(MapArea obj) {
        return mService.create(obj);
    }

    @Override
    public Call<MapArea> update(MapArea obj) {
        return mService.update(obj);
    }

    @Override
    public Call<MapArea> delete(Integer id) {
        return mService.delete(id);
    }

    @Override
    public Call<List<MapArea>> query(DataQueryCriteria criteria) {
        return mService.query(criteria.build());
    }

    @Override
    public Call<List<MapArea>> query(DataApiQuery query) {
        return mService.query(new DataApiQuery[] {query});
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }

}
