package com.fixit.core.rest;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Kostyantin on 12/20/2016.
 */

public interface ServerDataAPI<E> extends ServerAPI {
    Call<E> find(Integer id);
    Call<List<E>> findAll();
    Call<E> create(E obj);
    Call<E> update(E obj);
    Call<E> delete(Integer id);
}
