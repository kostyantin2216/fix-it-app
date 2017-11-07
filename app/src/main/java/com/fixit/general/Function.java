package com.fixit.general;

/**
 * Created by Kostyantin on 11/2/2017.
 */

public interface Function<T, R> {
    R apply(T t);
}
