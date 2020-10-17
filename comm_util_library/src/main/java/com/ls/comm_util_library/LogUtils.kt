package com.ls.comm_util_library

import android.util.Log

/**
* @FileName: LogUtils.kt
* @Description: 自定义日志输出
* @Author: ls
* @Date: 2019/7/22 14:40
*/
object LogUtils {
    private var TAG = "Custom-APP"

    private val LOG_MAX_LENGTH = 1 * 1024

    private var isDebug = false

    private fun logX(tag: String,log: String,print: ISingleListener<String>){
        var size = log.length / LOG_MAX_LENGTH
        if(log.length % LOG_MAX_LENGTH > 0){
            size++
        }
        var prefix = "$tag: "
        for (i in 0 until size){
            if((i + 1) * LOG_MAX_LENGTH <= log.length) {
                print.onValue(prefix + "($i)" + log.substring(i * LOG_MAX_LENGTH, (i + 1) * LOG_MAX_LENGTH))
            }
            else{
                print.onValue(prefix + "($i)"  + log.substring(i * LOG_MAX_LENGTH, log.length))
            }
        }
    }

    fun setDebug(debug: Boolean){
        isDebug = debug
    }

    /**
     * @param tag app的统一tag
     */
    fun setTag(tag: String){
        TAG = tag
    }

    /**
     * @param tag 二级tag
     * @param content 日志内容
     */
    fun v(tag: String, content: String) {
        if(isDebug){
            e(tag,content)
        }
        else{
            logX(tag,content, ISingleListener { Log.v(TAG,it) })
        }

    }

    /**
     * @param tag 二级tag
     * @param content 日志内容
     */
    fun d(tag: String,content: String) {
        if(isDebug){
            e(tag,content)
        }
        else {
            logX(tag,content, ISingleListener { Log.d(TAG,it) })
        }
    }

    /**
     * @param tag 二级tag
     * @param content 日志内容
     */
    fun i(tag: String, content: String) {
        if(isDebug){
            e(tag,content)
        }
        else {
            logX(tag,content, ISingleListener { Log.i(TAG,it) })
        }
    }

    /**
     * @param tag 二级tag
     * @param content 日志内容
     */
    fun w(tag: String, content: String) {
        if(isDebug){
            e(tag,content)
        }
        else {
            logX(tag,content, ISingleListener { Log.w(TAG,it) })
        }
    }

    /**
     * @param tag 二级tag
     * @param content 日志内容
     */
    fun e(tag: String, content: String) {
        logX(tag,content, ISingleListener { Log.e(TAG,it) })
    }


}
