package com.ls.comm_util_library;

public interface IStateListener<T> {
    void onSuccess(T t);
    void onFailed(Exception e);
}
