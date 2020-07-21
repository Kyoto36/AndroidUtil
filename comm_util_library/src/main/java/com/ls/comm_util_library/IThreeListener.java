package com.ls.comm_util_library;

public interface IThreeListener<T,S,U> {
    void onValue(T t, S s, U u);
}
