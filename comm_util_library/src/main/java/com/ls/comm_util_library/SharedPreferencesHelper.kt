package com.ls.comm_util_library

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.ConcurrentHashMap

/**
 * @ClassName: SharedPreferencesHelper
 * @Description:
 * @Author: ls
 * @Date: 2020/9/11 12:05
 */
class SharedPreferencesHelper {
    companion object{
        private val spMap by lazy {
            ConcurrentHashMap<String,SharedPreferencesHelper>()
        }
        private lateinit var context: Context
        fun init(context: Context){
            this.context = context
        }

        fun get(fileName: String): SharedPreferencesHelper{
            var sph = spMap.get(fileName)
            if(sph == null){
                sph = SharedPreferencesHelper(context, fileName)
                spMap[fileName] = sph
            }
            return sph
        }
    }

    private val mSP: SharedPreferences;
    private constructor(context: Context,fileName: String){
        mSP = Companion.context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
    }

    fun getInt(key: String): Int{
        return mSP.getInt(key,-1)
    }

    fun setInt(key: String,value: Int){
        mSP.edit().putInt(key,value).apply()
    }

    fun getLong(key: String): Long{
        return mSP.getLong(key,-1)
    }

    fun setLong(key: String,value: Long){
        mSP.edit().putLong(key,value).apply()
    }

    fun getString(key: String): String{
        return mSP.getString(key,"")?:""
    }

    fun setString(key: String,value: String){
        mSP.edit().putString(key,value).apply()
    }

    fun remove(key: String){
        mSP.edit().remove(key).apply()
    }

    fun getBoolean(key: String,default: Boolean = false): Boolean{
        return mSP.getBoolean(key,default)
    }

    fun setBoolean(key: String,value: Boolean){
        mSP.edit().putBoolean(key,value).apply()
    }
}