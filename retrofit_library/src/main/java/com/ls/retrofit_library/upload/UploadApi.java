package com.ls.retrofit_library.upload;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Url;

public interface UploadApi {

    /**
     * 文件上传
     * @param body 需要上传的参数和文件的集合
     * @param <T> 接口返回对象泛型
     * @return
     */
    <T> Observable<T> upload(@Url String url, @Body RequestBody body);
}
