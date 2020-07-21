package com.ls.retrofit_library.download

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @ClassName: DownloadService
 * @Description:
 * @Author: ls
 * @Date: 2020/3/26 17:22
 */
interface DownloadApi {
    /*断点续传下载接口*/
    @Streaming
    /*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    fun download(@Header("RANGE") start: String?, @Url url: String?): Observable<ResponseBody?>
}