package com.ls.comm_util_library

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPrefrencesUtils private constructor(){

    private var mContext: Context? = null

    companion object{

        @SuppressLint("StaticFieldLeak")
        private var sInstance: SharedPrefrencesUtils? = null

        private var sDefaultName: String = "default"

        fun setFileName(name: String){
            sDefaultName = name
        }

        /**
         * @return 单例对象
         */
        private fun get(): SharedPrefrencesUtils {
            if (sInstance == null) {
                synchronized(ThreadUtils::class.java) {
                    if (sInstance == null) {
                        sInstance = SharedPrefrencesUtils()
                    }
                }
            }
            return sInstance!!
        }

        fun init(context: Context){
           get().mContext = context.applicationContext
        }

        fun getInt(key: String): Int{
            return getSharedPrefrences().getInt(key,-1)
        }

        fun setInt(key: String,value: Int){
            getSharedPrefrences().edit().putInt(key,value).apply()
        }

        fun getString(key: String): String?{
            return getSharedPrefrences().getString(key,"")
        }

        fun setString(key: String,value: String){
            getSharedPrefrences().edit().putString(key,value).apply()
        }

        fun getBoolean(key: String): Boolean{
            return getSharedPrefrences().getBoolean(key,false)
        }

        fun setBoolean(key: String,value: Boolean){
            getSharedPrefrences().edit().putBoolean(key,value).apply()
        }

        private fun getSharedPrefrences(): SharedPreferences{
            return get().mContext!!.getSharedPreferences(sDefaultName,Context.MODE_PRIVATE)
        }
    }
}
