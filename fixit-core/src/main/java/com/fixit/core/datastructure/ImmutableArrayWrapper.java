package com.fixit.core.datastructure;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kostyantin on 12/24/2016.
 */

public class ImmutableArrayWrapper<T> {

    private final T[] arr;

    @SuppressWarnings("unchecked")
    public ImmutableArrayWrapper(Collection<T> collection, Class<T> typeClass) {
        arr = collection.toArray((T[]) Array.newInstance(typeClass, collection.size()));
    }

    @SuppressWarnings("unchecked")
    public ImmutableArrayWrapper(List<T> list, Class<T> typeClass) {
        arr = list.toArray((T[]) Array.newInstance(typeClass, list.size()));
    }

    public T get(int index) {
        return arr[index];
    }

    public int size() {
        return arr.length;
    }

}
