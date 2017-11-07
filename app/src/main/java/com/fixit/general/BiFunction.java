package com.fixit.general;

/**
 * Created by Kostyantin on 11/2/2017.
 */

public interface BiFunction<T, U, R> {
    R apply(T t, U u);
}
