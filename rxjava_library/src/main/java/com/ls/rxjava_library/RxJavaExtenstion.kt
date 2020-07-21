package com.ls.rxjava_library

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.functions.BiConsumer
import io.reactivex.plugins.RxJavaPlugins

/**
 * @ClassName: RxJavaExtenstion
 * @Description:
 * @Author: ls
 * @Date: 2020/4/9 12:00
 */

fun <T> Observable<T>.subscribe(biConsumer: BiConsumer<in T, Disposable?>){
    subscribe(object : Observer<T> {
        private var disposable: Disposable? = null
        override fun onComplete() {
            if(disposable != null && !disposable!!.isDisposed){
                disposable!!.dispose()
            }
        }

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(t: T) {
            biConsumer.accept(t,disposable)
        }

        override fun onError(e: Throwable) {
            RxJavaPlugins.onError(OnErrorNotImplementedException(e))
        }
    })
}

fun Disposable.addControl(disposableControl: DisposableControl){
    disposableControl.addDisposable(this)
}

class RxJavaExtension {

    companion object{
        fun <T> subscribe(observable: Observable<T>, biConsumer: BiConsumer<in T, Disposable?>){
            observable.subscribe(biConsumer)
        }

        fun addControl(disposable: Disposable,disposableControl: DisposableControl){
            disposable.addControl(disposableControl)
        }
    }
}