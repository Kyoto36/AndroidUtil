package com.ls.retrofit_library;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface OtherApiService {

    /**
     * 独立路径请求 POST
     * @param headers 请求头
     * @return
     */
    @POST()
    <T> Observable<T> postHeader(@Url String url, @HeaderMap Map<String, String> headers);

    /**
     * 独立路径请求 POST
     * @param body 请求体
     * @return
     */
    @POST()
    <T> Observable<T> postBody(@Url String url, @Body Map<String,String> body);

    /**
     * 独立路径请求 POST
     * @param headers 请求头
     * @param body 请求体
     * @return
     */
    @POST()
    <T> Observable<T> post(@Url String url, @HeaderMap Map<String, String> headers, @Body Map<String,String> body);

    /**
     * 独立路径请求 GET
     * @param headers 请求头
     * @return
     */
    @GET()
    <T> Observable<T> getHeader(@Url String url, @HeaderMap Map<String, String> headers);

    /**
     * 独立路径请求 GET
     * @param body 请求参数，拼接在url后面
     * @return
     */
    @GET()
    <T> Observable<T> getBody(@Url String url, @QueryMap Map<String,String> body);

    /**
     * 独立路径请求 GET
     * @param headers 请求头
     * @param body 请求参数，拼接在url后面
     * @return
     */
    @GET()
    <T> Observable<T> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String,String> body);
}
