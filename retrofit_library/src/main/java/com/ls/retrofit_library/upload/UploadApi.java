package com.ls.retrofit_library.upload;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface UploadApi {

    /**
     * 通用文件上传
     * @param body 需要上传的参数和文件的集合
     * @return
     */
    @POST
    Observable<String> upload(@Url String url, @Body RequestBody body);

    /**
     * 通用文件上传
     * @param headers 请求头
     * @param body 需要上传的参数和文件的集合
     * @return
     */
    @POST
    Observable<String> upload(@Url String url, @HeaderMap Map<String,String> headers, @Body RequestBody body);

    /**
     * 单文件上传
     * @param url 上传接口
     * @return file 用文件生成的body
     */
    @Multipart
    @POST
    Observable<String> upload(@Url String url,@Part MultipartBody.Part file);

    /**
     * 单文件上传
     * @param url 上传接口
     * @param headers 请求头
     * @return
     */
    @Multipart
    @POST
    Observable<String> upload(@Url String url, @HeaderMap Map<String,String> headers,@Part MultipartBody.Part file);
}
