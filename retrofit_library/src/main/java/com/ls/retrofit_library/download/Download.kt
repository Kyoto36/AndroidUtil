package com.ls.retrofit_library.download

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.ls.comm_util_library.*
import com.ls.retrofit_library.RetrofitUtil
import com.ls.retrofit_library.db.DownloadInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
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
    companion object{
        private val PATH_KEY = "path:"
        private val URI_KEY = "uri:"
    }

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
                restart()
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

    private fun restart(){
        for (entity in mDownloadEntityMap.entries){
            if(entity.value.isStopByNetWork){
                if(isUri(entity.key)){
                    start(entity.value.info.url,getUri(entity.key),entity.value.listener)
                }
                else {
                    start(entity.value.info.url, getPath(entity.key), entity.value.listener)
                }
            }
        }
    }

    private fun getDownloadEntity(key: String,url: String,listener: IProgressListener<String>,start: Long): DownloadEntity?{
        var entity = mDownloadEntityMap[key]
        if(entity == null || !TextUtils.equals(entity.info.url,url)){
            entity = DownloadEntity()
            val list = mDownloadDao.query(url,key)
            if(list.isEmpty() || list.size > 1){
                entity.info = generateInfo(url, key)
            }
            else{
                entity.info = list[0]
            }
        }
        else if(TextUtils.equals(entity.info.url,url) && !entity.disposable.isDisposed){
            entity.listener = listener
            return null
        }
        entity.listener = listener
        if(start != 0L) {
            entity.info.alreadySize = start
        }
        if(entity.info.alreadySize >= entity.info.totalSize){
            entity.info.alreadySize = 0
        }
        return entity
    }

    private fun getUriKey(uri: Uri): String{
        return URI_KEY + uri.toString()
    }

    private fun getUri(key: String): Uri{
        val uriStr = key.substring(URI_KEY.length,key.length)
        return Uri.parse(uriStr)
    }

    private fun isUri(key: String): Boolean{
        return key.startsWith(URI_KEY,false)
    }

    private fun getPathKey(savePath: String): String{
        return PATH_KEY + savePath
    }

    private fun getPath(key: String): String{
        return key.substring(PATH_KEY.length,key.length)
    }

    private fun isPath(key: String): Boolean{
        return key.startsWith(PATH_KEY,false)
    }

    private fun start(download: Observable<Unit>,entity: DownloadEntity): Disposable{
        return download.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    entity.info.downState = 1
                    mDownloadDao.save(entity.info)
                    entity.listener.onFinish("")
                }, {
                    entity.info.downState = -1
                    mDownloadDao.save(entity.info)
                    if(it is SocketException || it is UnknownHostException || it is HttpException) {
                        entity.isStopByNetWork = true
                    }
                    it.printStackTrace()
                    entity.listener.onFailed(Exception(it))
                })
    }

    // 单线程下载
    fun start(url: String,uri: Uri,listener: IProgressListener<String>){
        val start = AndroidFileUtils.getFileSizeByUri(mContext,uri)
        val entity = getDownloadEntity(getUriKey(uri),url, listener,start) ?: return
        // wa 是追加写入的意思
        val out = mContext.contentResolver.openOutputStream(uri,"wa")
        val download = mDownloadApi.download("bytes=$start-",url)
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWhenNetworkException())
                .map {
                    entity.info.totalSize = start + it.contentLength()
                    writeOutputStream(it,out,entity)
                }
        entity.disposable = start(download,entity)
        mDownloadEntityMap[getUriKey(uri)] = entity
    }

    // 单线程下载
    fun start(url: String,savePath: String,listener: IProgressListener<String>){
        val entity = getDownloadEntity(getPathKey(savePath),url, listener,0) ?: return
        var start = 0L
        val file = File(savePath)
        if(file.exists() && entity.info.alreadySize != 0L && entity.info.downState != 1){
            start = entity.info.alreadySize
        }
        if(start == 0L && file.exists()){
            file.delete()
        }
        entity.info.alreadySize = start
        val download = mDownloadApi.download("bytes=$start-",url)
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
        entity.disposable = start(download, entity)
        mDownloadEntityMap[getPathKey(savePath)] = entity
    }

    private fun generateInfo(url: String,savePath: String): DownloadInfo {
        val info = DownloadInfo()
        info.url = url
        info.savePath = savePath
        return info
    }

    // 同一个url可能会下载多个存在不同的地方，所以停止需要用savePath
    fun stop(savePath: String){
        stopKey(getPathKey(savePath))
    }

    fun stop(uri: Uri){
        stopKey(getUriKey(uri))
    }

    private fun stopKey(key: String){
        val entity = mDownloadEntityMap[key]
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
        FileUtils.write(responseBody.byteStream(),out,object : FileUtils.IWriteListener{
            override fun onSuccess() {
//                mHandler.post {
//                    entity.listener?.onFinish("")
//                }
            }
            override fun onError(e: java.lang.Exception?) {
                mHandler.post {
                    entity.listener?.onFailed(e)
                }
            }
            override fun onWrite(length: Long) {
                entity.info.downState = 0
                entity.info.alreadySize = length
                mDownloadDao.save(entity.info)
                mHandler.post {
                    entity.listener?.onProgress(length, entity.info.totalSize)
                }
            }
        })

//        val bufferedOut = if(out !is BufferedOutputStream) BufferedOutputStream(out) else out
//        var ins = responseBody.byteStream()
//        ins = if(ins !is BufferedInputStream) BufferedInputStream(ins) else ins
//        try {
//            val buffer = ByteArray(1024 * 6)
//            var len = 0
//            var record = entity.info.alreadySize
//            while (ins.read(buffer).also { len = it } != -1) {
//                bufferedOut.write(buffer, 0, len)
//                record += len
//                entity.info.downState = 0
//                entity.info.alreadySize = record
//                mHandler.post {
//                    entity.listener?.onProgress(record, entity.info.totalSize)
//                }
//                mDownloadDao.save(entity.info)
//            }
//        }
//        catch (e: IOException){
//            throw e
//        }
//        finally {
//            try {
//                bufferedOut.flush()
//                ins.close()
//                bufferedOut.close()
//            }
//            catch (e: IOException){
//                e.printStackTrace()
//            }
//        }

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
        val allLength = entity.info.totalSize
        FileUtils.writeRandomAccessFile(responseBody.byteStream(),file,entity.info.alreadySize,allLength, object :FileUtils.IWriteListener {
            override fun onSuccess() {
                mHandler.post {
//                    entity.listener?.onFinish("")
                }
            }
            override fun onError(e: java.lang.Exception?) {
                mHandler.post {
                    entity.listener?.onFailed(e)
                }
            }
            override fun onWrite(length: Long) {
                entity.info.downState = 0
                entity.info.alreadySize = length
                mDownloadDao.save(entity.info)
                mHandler.post {
                    entity.listener?.onProgress(length, allLength)
                }

            }
        })

//        var randomAccessFile: RandomAccessFile? = null
//        try {
//            if (!file.parentFile.exists()) file.parentFile.mkdirs()
//
//            randomAccessFile = RandomAccessFile(file, "rwd")
//
//            // 这种方式会出现oom
////        val channelOut = randomAccessFile.channel
////        val mappedBuffer = channelOut.map(
////            FileChannel.MapMode.READ_WRITE,
////            entity.info.alreadySize, allLength - entity.info.alreadySize
////        )
//            randomAccessFile.setLength(allLength)
//            randomAccessFile.seek(entity.info.alreadySize)
//            val buffer = ByteArray(1024 * 6)
//            var len: Int
//            var record = entity.info.alreadySize
//            while (responseBody.byteStream().read(buffer).also { len = it } != -1) {
//                randomAccessFile.write(buffer, 0, len)
////            mappedBuffer.put(buffer, 0, len)
//                record += len
//                entity.info.downState = 0
//                entity.info.alreadySize = record
//                mHandler.post {
//                    entity.listener?.onProgress(record, allLength)
//                }
//                mDownloadDao.save(entity.info)
//            }
//        }
//        catch (e: IOException){
//            throw e
//        }
//        finally {
//            try{
//                responseBody.byteStream().close()
//                randomAccessFile?.close()
//            }
//            catch (e: IOException){
//                e.printStackTrace()
//            }
//        }
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

        fun build(context: Context): Download{
            if(mRetrofit == null){
                mRetrofit = RetrofitUtil.generateRetrofit(mBaseUrl)
            }
            return Download(context, mRetrofit!!)
        }
    }
}