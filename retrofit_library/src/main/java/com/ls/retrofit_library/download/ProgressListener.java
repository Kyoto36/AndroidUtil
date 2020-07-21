package com.ls.retrofit_library.download;

public interface ProgressListener{
        void onProgress(long progress,long total);
        void onSuccess();
        void onError(Exception e);
    }