package com.ls.retrofit_library.upload

import android.content.Context
import android.os.Handler
import com.ls.retrofit_library.RetrofitUtil
import okhttp3.MultipartBody
import retrofit2.Retrofit
import java.io.File

/**
 * @ClassName: Upload
 * @Description:
 * @Author: ls
 * @Date: 2020/7/16 13:52
 */
class Upload {

    private val mContext: Context
    private val mRetrofit: Retrofit
    private val mHandler: Handler
    private val mUploadApi: UploadApi

    private constructor(context: Context,retrofit: Retrofit){
        mContext = context
        mRetrofit = retrofit
        mUploadApi = mRetrofit.create(UploadApi::class.java)
        mHandler = Handler()
    }

    /**
     * 单文件上传
     * @param body 接口参数（出文件外）
     * @param fileKey 文件参数的key
     * @param file 需要上传的文件
     */
    fun start(body: Map<String,String>?,fileKey: String ,file: File){
        val builder = MultipartBody.Builder()
        if(body != null){
            for (item in body){
                builder.addFormDataPart(item.key,item.value)
            }
        }
//        val part = MultipartBody.Part.createFormData("picture", file.name, fileRQ)

//        builder.addFormDataPart(fileKey,file.name,)
    }

    /**
     * 多文件上传
     * @param body 接口参数
     * @param fileMap 文件的参数key和文件一一对应
     */
    fun start(body: Map<String, String>,fileMap: Map<String,File>){

    }

    class Builder{
        private var mBaseUrl: String? = null
        private var mRetrofit: Retrofit? = null

        /**
         * 与BaseUrl相对应，传了Retrofit之后BaseUrl就失效了
         */
        fun setRetrofit(retrofit: Retrofit){
            mRetrofit = retrofit
        }

        fun setBaseUrl(baseUrl: String){
            mBaseUrl = baseUrl
        }

        fun build(context: Context): Upload{
            if(mRetrofit == null){
                mRetrofit = RetrofitUtil.generateRetrofit(mBaseUrl)
            }
            return Upload(context, mRetrofit!!)
        }
    }
}