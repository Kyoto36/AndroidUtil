package com.ls.retrofit_library;

import android.text.TextUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public abstract class RetrofitUtil {
    public static Retrofit generateRetrofit(String baseUrl){
        if(TextUtils.isEmpty(baseUrl)){
            baseUrl = "http://www.baidu.com";
        }
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }
}
