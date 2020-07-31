package com.ls.retrofit_library.download;

import com.ls.comm_util_library.IProgressListener;
import com.ls.retrofit_library.db.DownloadInfo;

import io.reactivex.disposables.Disposable;

public class DownloadEntity {
    private DownloadInfo info;
    private boolean stopByNetWork;
    private Disposable disposable;
    private IProgressListener<String> listener;

    public DownloadInfo getInfo() {
        return info;
    }

    public void setInfo(DownloadInfo info) {
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

    public IProgressListener<String> getListener() {
        return listener;
    }

    public void setListener(IProgressListener<String> listener) {
        this.listener = listener;
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "info=" + info +
                ", stopByNetWork=" + stopByNetWork +
                '}';
    }
}
