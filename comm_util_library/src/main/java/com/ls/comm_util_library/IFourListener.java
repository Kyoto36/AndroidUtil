package com.ls.comm_util_library;

public interface IFourListener<T,S,U,V> {
    void onValue(T t, S s, U u, V v);
}
