package com.fixit.core.rest.apis;

import com.fixit.core.data.MapArea;
import com.fixit.core.rest.ServerDataAPI;
import com.fixit.core.rest.services.MapAreaService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public class MapAreaDataAPI implements ServerDataAPI<MapArea> {

    public final static String API_NAME = "data/MapAreas";

    private final MapAreaService mService;

    public MapAreaDataAPI(MapAreaService service) {
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
    public String getApiName() {
        return API_NAME;
    }

}
