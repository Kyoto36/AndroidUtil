package com.ls.rxjava_library

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @ClassName: DisposableControl
 * @Description:
 * @Author: ls
 * @Date: 2020/4/3 16:52
 */
class DisposableControl : LifecycleObserver {

    private var mDisposables: MutableList<Disposable?>? = null

    fun addDisposable(disposable: Disposable?) {
        if (mDisposables == null) {
            mDisposables = ArrayList()
        }
        mDisposables!!.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        if (mDisposables != null) {
            for (disposable in mDisposables!!) {
                if (disposable != null && !disposable.isDisposed) {
                    disposable.dispose()
                }
            }
        }
    }
}