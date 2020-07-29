package com.ls.retrofit_library.upload

import android.content.Context
import android.os.Handler
import android.util.Log
import com.ls.comm_util_library.*
import com.ls.retrofit_library.RetrofitUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception

/**
 * @ClassName: Upload
 * @Description:
 * @Author: ls
 * @Date: 2020/7/16 13:52
 */
class Upload {

    companion object{
        private val TAG = "Upload"
    }

    private val mContext: Context
    private val mRetrofit: Retrofit
    private val mHandler: Handler
    private val mUploadApi: UploadApi
    private val mUploadMap : MutableMap<String,Disposable>

    private constructor(context: Context,retrofit: Retrofit){
        mContext = context
        mRetrofit = retrofit
        mUploadApi = mRetrofit.create(UploadApi::class.java)
        mHandler = Handler()
        mUploadMap = HashMap()
    }

    /**
     * 单文件上传
     * @param url 上传接口
     * @param headers 请求头
     * @param body 接口参数（出文件外）
     * @param fileKey 文件参数的key
     * @param file 需要上传的文件
     * @return 上传的唯一码，可用于停止上传
     */
    fun start(url: String,headers: Map<String,String>?,body: Map<String,String>?,fileKey: String ,file: File,listener: IProgressListener<String>): String{
        /*var fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        fileRQ = UploadRequestBody(fileRQ,object : IDoubleListener<Long,Long>{
            override fun onValue(progress: Long, total: Long) {
                LogUtils.d(TAG,"progress = $progress total = $total")
            }

        })
        val part = MultipartBody.Part.createFormData(fileKey, file.name, fileRQ)
        mUploadApi.upload(url,headers,part)
                .subscribe({
                    Log.d(TAG,"result = ${it.toString()}")
                },{
                    LogUtils.d(TAG,"" + it.message)
                    it.printStackTrace()
                })*/
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if(body != null){
            for (item in body){
                builder.addFormDataPart(item.key,item.value)
            }
        }
        builder.addFormDataPart(fileKey,file.name,getFileRQ(file,listener))
        val uploadKey = TxtUtils.randomString(5) + System.currentTimeMillis()
        mUploadMap[uploadKey] = getUpload(url,headers,builder.build())
                .subscribe({
                    listener.onFinish(it)
                },{
                    listener.onFailed(Exception(it))
                })
        return uploadKey
    }

    /**
     * 多文件上传
     * @param url 上传接口
     * @param headers 请求头
     * @param body 接口参数
     * @param fileMap 文件的参数key和文件一一对应
     */
    fun start(url: String,headers: Map<String,String>?,body: Map<String, String>?,fileMap: Map<String,File>,listener: IProgressListener<String>): String{
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if(body != null){
            for (item in body){
                builder.addFormDataPart(item.key,item.value)
            }
        }
        var fileRQ: RequestBody
        for (item in fileMap){
            fileRQ = getFileRQ(item.value,listener)
            builder.addFormDataPart(item.key,item.value.name,fileRQ)
        }
        val uploadKey = TxtUtils.randomString(5) + System.currentTimeMillis()
        mUploadMap[uploadKey] = getUpload(url,headers,builder.build())
                .subscribe({
                    listener.onFinish(it)
                },{
                    listener.onFailed(Exception(it))
                })
        return uploadKey
    }

    fun destroy(){
        stopAll()
    }

    fun stop(uploadKey: String){
        val disposable = mUploadMap[uploadKey]
        if(null != disposable && !disposable.isDisposed){
            disposable.dispose()
        }
    }

    fun stopAll(){
        for (item in mUploadMap){
            if(null != item.value && !item.value.isDisposed){
                item.value.dispose()
            }
        }
    }

    private fun getUpload(url: String,headers: Map<String,String>?,body: MultipartBody): Observable<String>{
        return if(headers == null){
            mUploadApi.upload(url, body)
        }
        else {
            mUploadApi.upload(url, headers, body)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getFileRQ(file: File,listener: IProgressListener<String>): RequestBody{
        var fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        fileRQ = UploadRequestBody(fileRQ, IDoubleListener { progress, total ->
            listener.onProgress(progress,total)
        })
        return fileRQ
    }

    class Builder{
        private var mBaseUrl: String? = null
        private var mRetrofit: Retrofit? = null

        /**
         * 与BaseUrl相对应，传了Retrofit之后BaseUrl就失效了
         */
        fun setRetrofit(retrofit: Retrofit): Builder{
            mRetrofit = retrofit
            return this
        }

        fun setBaseUrl(baseUrl: String): Builder{
            mBaseUrl = baseUrl
            return this
        }

        fun build(context: Context): Upload{
            if(mRetrofit == null){
                mRetrofit = RetrofitUtil.generateRetrofit(mBaseUrl)
            }
            return Upload(context, mRetrofit!!)
        }
    }
}