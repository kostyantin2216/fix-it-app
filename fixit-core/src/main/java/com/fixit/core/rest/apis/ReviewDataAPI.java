package com.fixit.core.rest.apis;

import com.fixit.core.data.Review;
import com.fixit.core.rest.queries.DataApiQuery;
import com.fixit.core.rest.queries.DataQueryCriteria;
import com.fixit.core.rest.queries.DataQueryRequest;
import com.fixit.core.rest.services.ReviewDataService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by konstantin on 5/17/2017.
 */

public class ReviewDataAPI implements ServerDataAPI<Review> {

    public final static String API_NAME = "data/Reviews";

    private final ReviewDataService mService;

    public ReviewDataAPI(ReviewDataService service) {
        mService = service;
    }

    @Override
    public Call<Review> find(Integer id) {
        return mService.find(id);
    }

    @Override
    public Call<List<Review>> findAll() {
        return mService.findAll();
    }

    @Override
    public Call<Review> create(Review obj) {
        return mService.create(obj);
    }

    @Override
    public Call<Review> update(Review obj) {
        return mService.update(obj);
    }

    @Override
    public Call<Review> delete(Integer id) {
        return mService.delete(id);
    }

    @Override
    public Call<List<Review>> query(DataQueryCriteria criteria) {
        return mService.query(criteria.build());
    }

    @Override
    public Call<List<Review>> query(DataApiQuery query) {
        return mService.query(new DataQueryRequest(query));
    }

    @Override
    public String getApiName() {
        return API_NAME;
    }
}
