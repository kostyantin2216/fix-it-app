package com.fixit.core.rest.apis;

import com.fixit.core.data.Profession;
import com.fixit.core.rest.ServerDataAPI;
import com.fixit.core.rest.services.ProfessionService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 12/23/2016.
 */

public class ProfessionDataAPI implements ServerDataAPI<Profession> {

    public final static String API_NAME = "data/Professions";

    private ProfessionService mService;

    public ProfessionDataAPI(ProfessionService service) {
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
    public String getApiName() {
        return API_NAME;
    }
}
