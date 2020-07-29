package com.ls.comm_util_library;

public interface IProgressListener<T> {
    void onProgress(long progress,long total);
    void onFinish(T t);
    void onFailed(Exception e);
}
