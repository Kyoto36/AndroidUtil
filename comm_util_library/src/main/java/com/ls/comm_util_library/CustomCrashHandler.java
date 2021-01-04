package com.ls.comm_util_library;

import android.app.Application;

public class CustomCrashHandler implements Thread.UncaughtExceptionHandler {
 
    Thread.UncaughtExceptionHandler exceptionHandler;
    static CustomCrashHandler instance;
 
    public CustomCrashHandler(Application application, Thread.UncaughtExceptionHandler handler) {
        exceptionHandler = handler;
        instance = this;
    }
 
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        AppUtils.exitApp();
        if (exceptionHandler != null) {
            exceptionHandler.uncaughtException(t, e);
        }
    }
 
    public static CustomCrashHandler getInstance() {
        return instance;
    }

}