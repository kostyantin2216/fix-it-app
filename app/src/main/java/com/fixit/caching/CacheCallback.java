package com.fixit.caching;

import com.fixit.rest.callbacks.GeneralServiceErrorCallback;

import java.util.List;

/**
 * Created by Kostyantin on 8/26/2017.
 */

public interface CacheCallback<T> extends GeneralServiceErrorCallback {
    void onResult(T[] results);
}
