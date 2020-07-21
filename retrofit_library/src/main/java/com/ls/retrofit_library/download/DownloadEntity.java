package com.ls.retrofit_library.download;

import io.reactivex.disposables.Disposable;

public class DownloadEntity {
    private DBHelper.DownloadInfo info;
    private boolean stopByNetWork;
    private Disposable disposable;
    private ProgressListener listener;

    public DBHelper.DownloadInfo getInfo() {
        return info;
    }

    public void setInfo(DBHelper.DownloadInfo info) {
        this.info = info;
    }

    public boolean isStopByNetWork() {
        return stopByNetWork;
    }

    public void setStopByNetWork(boolean stopByNetWork) {
        this.stopByNetWork = stopByNetWork;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public void setDisposable(Disposable disposable) {
        this.disposable = disposable;
    }

    public ProgressListener getListener() {
        return listener;
    }

    public void setListener(ProgressListener listener) {
        this.listener = listener;
    }


}
