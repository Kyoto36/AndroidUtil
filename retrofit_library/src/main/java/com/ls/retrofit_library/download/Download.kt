package com.ls.retrofit_library.download

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import com.ls.comm_util_library.AndroidFileUtils
import com.ls.comm_util_library.NetworkListener
import com.ls.comm_util_library.NetworkReceiver
import com.ls.comm_util_library.NetworkUtils
import com.ls.retrofit_library.RetrofitUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.*
import java.net.SocketException
import java.net.UnknownHostException

/**
 * @ClassName: DownloadManager
 * @Description:
 * @Author: ls
 * @Date: 2020/3/27 12:53
 */
class Download {
    private val mDownloadApi: DownloadApi
    private val mDownloadEntityMap: MutableMap<String, DownloadEntity> = HashMap()
    private val mDownloadDao: DownloadInfoDao

    private val mHandler: Handler
    private val mContext: Context
    private val mRetrofit: Retrofit

    private var mReceiver = NetworkReceiver()
    private val mNetWorkListener = object :NetworkListener{
        override fun onChange(type: NetworkUtils.Types) {
            if(type != NetworkUtils.Types.NONE){
                for (value in mDownloadEntityMap.values){
                    if(value.isStopByNetWork){
                        start(value.info.url,value.info.savePath,value.listener)
                    }
                }
            }
        }
    }

    private constructor(context: Context,retrofit: Retrofit){
        mContext = context
        mRetrofit = retrofit
        mDownloadApi = mRetrofit.create(DownloadApi::class.java)
        mDownloadDao = DownloadInfoDao(context)
        mHandler = Handler()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mContext.registerReceiver(mReceiver,filter)
        mReceiver.addListener(mNetWorkListener)
    }

    // 单线程下载
    fun start(url: String,uri: Uri,listener: ProgressListener){
        var entity = mDownloadEntityMap[uri.toString()]
        var start = 0L
        if(entity == null || !TextUtils.equals(entity.info.url,url)){
            entity = DownloadEntity()
            val list = mDownloadDao.query(url,uri.toString())
            if(list.isEmpty() || list.size > 1){
                entity.info = generateInfo(url, uri.toString())
            }
            else{
                entity.info = list[0]
            }
        }
        else if(TextUtils.equals(entity.info.url,url) && !entity.disposable.isDisposed){
            entity.listener = listener
            return
        }
        start = AndroidFileUtils.getFileSizeByUri(mContext,uri)
        // wa 是追加写入的意思
        val out = mContext.contentResolver.openOutputStream(uri,"wa")
        entity.listener = listener
        entity.info.alreadySize = start
        entity.disposable = mDownloadApi.download("bytes=$start-",url)
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWhenNetworkException())
                .map {
                    entity.info.totalSize = start + it.contentLength()
                    writeOutputStream(it,out,entity)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    entity.info.downState = 1
                    mDownloadDao.save(entity.info)
                    listener.onSuccess()
                }, {
                    entity.info.downState = -1
                    mDownloadDao.save(entity.info)
                    if(it is SocketException || it is UnknownHostException) {
                        entity.isStopByNetWork = true
                    }
                    listener.onError(Exception(it))
                })
        mDownloadEntityMap[uri.toString()] = entity
    }

    // 单线程下载
    fun start(url: String,savePath: String,listener: ProgressListener){
        var entity = mDownloadEntityMap[savePath]
        var start = 0L
        val file = File(savePath)
        if(entity == null || !TextUtils.equals(entity.info.url,url)){
            entity = DownloadEntity()
            val list = mDownloadDao.query(url,savePath)
            if(list.isEmpty() || list.size > 1){
                entity.info = generateInfo(url, savePath)
            }
            else{
                entity.info = list[0]
            }
        }
        else if(TextUtils.equals(entity.info.url,url) && !entity.disposable.isDisposed){
            entity.listener = listener
            return
        }
        if(file.exists() && entity.info.alreadySize != 0L && entity.info.downState != 1){
            start = entity.info.alreadySize
        }
        if(start == 0L && file.exists()){
            file.delete()
        }
        entity.listener = listener
        entity.info.alreadySize = start
        entity.disposable = mDownloadApi.download("bytes=$start-",url)
            .flatMap {
                if(start == 0L || it.contentLength() + start == file.length()){
                    Observable.just(it)
                }
                else{
                    start = 0L
                    entity.info.alreadySize = start
                    if(file.exists()) {
                        file.delete()
                    }
                    mDownloadApi.download("bytes=$start-",url)
                }
            }
            /*指定线程*/
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWhenNetworkException())
            .map {
                entity.info.totalSize = start + it.contentLength()
                writeFile(it,File(savePath),entity)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                entity.info.downState = 1
                mDownloadDao.save(entity.info)
                listener.onSuccess()
            }, {
                entity.info.downState = -1
                mDownloadDao.save(entity.info)
                if(it is SocketException || it is UnknownHostException) {
                    entity.isStopByNetWork = true
                }
                listener.onError(Exception(it))
            })
        mDownloadEntityMap[savePath] = entity
    }


    private fun generateInfo(url: String,savePath: String): DBHelper.DownloadInfo {
        val info = DBHelper.DownloadInfo()
        info.url = url
        info.savePath = savePath
        return info
    }

    // 同一个url可能会下载多个存在不同的地方，所以停止需要用savePath
    fun stop(savePath: String){
        val entity = mDownloadEntityMap[savePath]
        if(entity != null && entity.disposable != null && !entity.disposable.isDisposed){
            entity.disposable.dispose()
        }
    }

    fun stop(uri: Uri){
        val entity = mDownloadEntityMap[uri.toString()]
        if(entity != null && entity.disposable != null && !entity.disposable.isDisposed){
            entity.disposable.dispose()
        }
    }

    fun stopAll(){
        for(value in mDownloadEntityMap.values){
            if(value != null && value.disposable != null && !value.disposable.isDisposed){
                value.disposable.dispose()
            }
        }
    }

    fun destroy(){
        stopAll()
        mReceiver.removeListener(mNetWorkListener)
        mContext.unregisterReceiver(mReceiver)
    }

    @Throws(IOException::class)
    private fun writeOutputStream(
          responseBody: ResponseBody,
          out: OutputStream?,
          entity: DownloadEntity){
        if(out == null) throw IOException("out is null !!!")
        val bufferedOut = if(out !is BufferedOutputStream) BufferedOutputStream(out) else out
        var ins = responseBody.byteStream()
        ins = if(ins !is BufferedInputStream) BufferedInputStream(ins) else ins
        try {
            val buffer = ByteArray(1024 * 4)
            var len = 0
            var record = entity.info.alreadySize
            while (ins.read(buffer).also { len = it } != -1) {
                bufferedOut.write(buffer, 0, len)
                record += len
                entity.info.downState = 0
                entity.info.alreadySize = record
                mHandler.post {
                    entity.listener?.onProgress(record, entity.info.totalSize)
                }
                mDownloadDao.save(entity.info)
            }
        }
        catch (e: IOException){
            throw e
        }
        finally {
            try {
                bufferedOut.flush()
                ins.close()
                bufferedOut.close()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }

    }

    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    @SuppressLint("CheckResult")
    @Throws(IOException::class)
    private fun writeFile(
        responseBody: ResponseBody,
        file: File,
        entity: DownloadEntity
    ) {
        var randomAccessFile: RandomAccessFile? = null
        try {
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            val allLength = entity.info.totalSize
            randomAccessFile = RandomAccessFile(file, "rwd")

            // 这种方式会出现oom
//        val channelOut = randomAccessFile.channel
//        val mappedBuffer = channelOut.map(
//            FileChannel.MapMode.READ_WRITE,
//            entity.info.alreadySize, allLength - entity.info.alreadySize
//        )
            randomAccessFile.setLength(allLength)
            randomAccessFile.seek(entity.info.alreadySize)
            val buffer = ByteArray(1024 * 4)
            var len: Int
            var record = entity.info.alreadySize
            while (responseBody.byteStream().read(buffer).also { len = it } != -1) {
                randomAccessFile.write(buffer, 0, len)
//            mappedBuffer.put(buffer, 0, len)
                record += len
                entity.info.downState = 0
                entity.info.alreadySize = record
                mHandler.post {
                    entity.listener?.onProgress(record, allLength)
                }
                mDownloadDao.save(entity.info)
            }
        }
        catch (e: IOException){
            throw e
        }
        finally {
            try{
                responseBody.byteStream().close()
                randomAccessFile?.close()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    class Builder{
        private var mRetrofit: Retrofit? = null
        private var mBaseUrl: String? = null

        /**
         * 与BaseUrl相对应，传了Retrofit之后BaseUrl就失效了
         */
        fun setRetrofit(retrofit: Retrofit?): Builder {
            mRetrofit = retrofit
            return this
        }

        fun setBaseUrl(baseUrl: String?): Builder {
            mBaseUrl = baseUrl
            return this
        }

        fun build(context: Context): Download?{
            if(mRetrofit == null){
                mRetrofit = RetrofitUtil.generateRetrofit(mBaseUrl)
            }
            return Download(context, mRetrofit!!)
        }
    }
}