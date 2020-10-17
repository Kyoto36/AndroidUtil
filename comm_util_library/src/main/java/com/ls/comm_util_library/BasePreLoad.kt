package com.ls.comm_util_library

/**
 * @ClassName: IPreLoad
 * @Description:
 * @Author: ls
 * @Date: 2019/12/12 11:25
 */
abstract class BasePreLoad {
    protected var mNoMore = false
    protected var isLoading = false
    protected var mInit = false
    protected var mNeedTrigger = false

    abstract fun isCanPreLoad(): Boolean

    abstract fun preLoad()

    fun init(){
        mInit = true
    }

    fun setNeedTrigger(needTrigger: Boolean){
        mNeedTrigger = needTrigger
    }

    open fun endLoad(){
        isLoading = false
    }

    open fun noMore(nomore: Boolean){
        mNoMore = nomore
    }

    protected open fun startLoad(){
        if(!mNeedTrigger && mInit && !mNoMore && !isLoading){
            isLoading = true
            preLoad()
        }
    }

    fun justLoad(){
        setNeedTrigger(false)
        startLoad()
    }
}