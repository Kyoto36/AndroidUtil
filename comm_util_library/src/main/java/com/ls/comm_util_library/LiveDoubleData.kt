package com.ls.comm_util_library

import androidx.lifecycle.MutableLiveData
import com.ls.comm_util_library.ThreadUtils

/**
 * @ClassName: LiveDoubleData
 * @Description:
 * @Author: ls
 * @Date: 2019/8/28 16:54
 */
class LiveDoubleData<T,S>: MutableLiveData<DoubleData<T, S>>() {

    fun notifyValue(t: T?,s: S?) = if(ThreadUtils.isMainThread) value = DoubleData(t,s) else postValue(DoubleData(t,s))

    fun notifyValueT(t: T?) = if(ThreadUtils.isMainThread) value = DoubleData(t,value?.s) else postValue(DoubleData(t,value?.s))

    fun notifyValueS(s: S?) = if(ThreadUtils.isMainThread) value = DoubleData(value?.t,s) else postValue(DoubleData(value?.t,s))

    fun getT(): T?{
        return value?.t
    }

    fun getS(): S?{
        return value?.s
    }

}