package com.fixit.core.rest.apis;

import com.fixit.core.data.AppInstallation;
import com.fixit.core.rest.ServerDataAPI;
import com.fixit.core.rest.services.AppInstallationService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by konstantin on 4/3/2017.
 */

public class AppInstallationAPI implements ServerDataAPI<AppInstallation> {

    public final static String API_NAME = "data/AppInstallations";

    private final AppInstallationService service;

    public AppInstallationAPI(AppInstallationService service) {
        this.service = service;
    }

    @Override
    public Call<AppInstallation> find(Integer id) {
        return service.find(id);
    }

    @Override
    public Call<List<AppInstallation>> findAll() {
        return service.findAll();
    }

    @Override
    public Call<AppInstallation> create(AppInstallation obj) {
        return service.create(obj);
    }

    @Override
    public Call<AppInstallation> update(AppInstallation obj) {
        return service.update(obj);
    }

    @Override
    public Call<AppInstallation> delete(Integer id) {
        return service.delete(id);
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
