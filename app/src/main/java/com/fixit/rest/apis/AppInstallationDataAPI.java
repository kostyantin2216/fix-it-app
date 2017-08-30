package com.fixit.rest.apis;

import com.fixit.data.AppInstallation;
import com.fixit.rest.queries.DataApiQuery;
import com.fixit.rest.queries.DataQueryCriteria;
import com.fixit.rest.services.AppInstallationDataService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by konstantin on 4/3/2017.
 */

public class AppInstallationDataAPI implements ServerDataAPI<AppInstallation> {

    public final static String API_NAME = "data/AppInstallations";

    private final AppInstallationDataService service;

    public AppInstallationDataAPI(AppInstallationDataService service) {
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
    public Call<List<AppInstallation>> query(DataQueryCriteria criteria) {
        return service.query(criteria.build());
    }

    @Override
    public Call<List<AppInstallation>> query(DataApiQuery query) {
        return service.query(new DataApiQuery[] {query});
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
