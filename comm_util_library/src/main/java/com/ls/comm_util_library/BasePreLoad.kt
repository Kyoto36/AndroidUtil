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

    abstract fun isCanPreLoad(): Boolean

    abstract fun preLoad()

    open fun endLoad(){
        isLoading = false
    }

    open fun noMore(nomore: Boolean){
        mNoMore = nomore
    }

    open fun startLoad(){
        if(!mNoMore && !isLoading){
            isLoading = true
            preLoad()
        }
    }
}