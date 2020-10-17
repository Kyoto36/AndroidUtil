package com.ls.comm_util_library

import androidx.lifecycle.MutableLiveData
import com.ls.comm_util_library.ThreadUtils

/**
 * @ClassName: LiveDoubleData
 * @Description:
 * @Author: ls
 * @Date: 2019/8/28 16:54
 */
open class LiveDoubleData<T,S>: MutableLiveData<DoubleData<T, S>>() {

    open fun notifyValue(t: T?,s: S?) = if(ThreadUtils.isMainThread) value = DoubleData(t,s) else postValue(DoubleData(t,s))

    open fun notifyValueT(t: T?) = if(ThreadUtils.isMainThread) value = DoubleData(t,value?.s) else postValue(DoubleData(t,value?.s))

    open fun notifyValueS(s: S?) = if(ThreadUtils.isMainThread) value = DoubleData(value?.t,s) else postValue(DoubleData(value?.t,s))

    fun getT(): T?{
        return value?.t
    }

    fun getS(): S?{
        return value?.s
    }

}