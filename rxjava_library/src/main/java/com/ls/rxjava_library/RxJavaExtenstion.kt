package com.ls.rxjava_library

import android.util.Log
import com.ls.comm_util_library.IResultListener
import com.ls.comm_util_library.ThreadUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.functions.BiConsumer
import io.reactivex.internal.operators.observable.ObservableMap
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Function as Fun

/**
 * @ClassName: RxJavaExtenstion
 * @Description:
 * @Author: ls
 * @Date: 2020/4/9 12:00
 */

/**
 * 订阅并自动取消订阅在OnComplete
 * @receiver Observable<T>
 * @param biConsumer Function2<T, Disposable?, Unit> 返回next和dispose数据
 */
fun <T> Observable<T>.subscribeAuto(biConsumer: (T, Disposable?) -> Unit) {
    subscribe(object : Observer<T> {
        private var disposable: Disposable? = null
        override fun onComplete() {
            if (disposable != null && !disposable!!.isDisposed) {
                disposable!!.dispose()
            }
        }

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(t: T) {
            biConsumer.invoke(t, disposable)
        }

        override fun onError(e: Throwable) {
            RxJavaPlugins.onError(OnErrorNotImplementedException(e))
        }
    })
}

/**
 * 订阅并自动取消订阅在第一个onNext之后
 * @receiver Observable<T>
 * @param consumer Function1<T, Unit> 返回next数据
 */
fun <T> Observable<T>.subscribeAuto(consumer: (T) -> Unit) {
    subscribeAuto { t, disposable ->
        consumer.invoke(t)
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }
}

/**
 * 订阅并自动取消订阅，忽略next数据
 * @receiver Observable<T>
 */
fun <T> Observable<T>.subscribeAuto() {
    subscribeAuto { t -> }
}

/**
 * 订阅并添加dispose控制
 * @receiver Observable<T>
 * @param subscribe Function1<T, Unit>
 * @param control DisposableControl dispose控制器
 */
fun <T> Observable<T>.subscribeAddControl(subscribe: (T) -> Unit, control: DisposableControl) {
    subscribe { subscribe.invoke(it) }
            .addControl(control)
}

/**
 * 订阅在主线程并添加dispose控制
 * @receiver Observable<T>
 * @param subscribe Function1<T, Unit>
 * @param control DisposableControl dispose控制器
 */
fun <T> Observable<T>.subscribeOnMainAddControl(subscribe: (T) -> Unit, control: DisposableControl) {
    observeOn(AndroidSchedulers.mainThread())
            .subscribe { subscribe.invoke(it) }
            .addControl(control)
}

/**
 * 订阅在主线程并添加dispose控制
 * @receiver Observable<T>
 * @param subscribe Function1<T, Unit>
 * @param control DisposableControl dispose控制器
 */
fun <T> Observable<T>.subscribeOnMainAddControl( subscribe: (T) -> Unit,complete: () -> Unit, control: DisposableControl) {
    observeOn(AndroidSchedulers.mainThread())
            .subscribe({subscribe.invoke(it)},{},{complete.invoke()})
            .addControl(control)
}

/**
 * 添加dispose控制，便于集体销毁
 * @receiver Disposable
 * @param disposableControl DisposableControl dispose控制器
 */
fun Disposable.addControl(disposableControl: DisposableControl) {
    disposableControl.addDisposable(this)
}

/**
 * 主要是同一页面的内容转换过快的问题解决
 * @receiver Observable<T>
 * @param stateMachine StateMachine
 * @param index Int
 * @param status Int
 * @return Observable<T>
 */
fun <T> Observable<T>.updateState(stateMachine: StateMachine, index: Int, status: Int): Observable<T> {
    val time = stateMachine.updateState(index, status)
    return filter { stateMachine.check(index, status, time) }.observeOn(AndroidSchedulers.mainThread())
}

/**
 * 转换到计算线程中，然后做计算操作
 * @receiver Observable<T>
 * @param mapper Function<in T, out R>
 * @return Observable<R>
 */
fun <T, R> Observable<T>.transform(mapper: Fun<in T, out R>): Observable<R> {
    return observeOn(Schedulers.computation()).map(mapper)
}

/**
 * 缓存数据，将任务丢到io线程中，然后让主线继续往下
 * @receiver Observable<T>
 * @param run Runnable
 * @return Observable<T>
 */
fun <T> Observable<T>.cache(run: (T) -> Unit): Observable<T> {
    return background(run)
}

/**
 * 将任务丢到io线程中，然后让主线继续往下
 * @receiver Observable<T>
 * @param run Function1<T, Unit>
 * @return Observable<T>
 */
fun <T> Observable<T>.background(run: (T) -> Unit): Observable<T>{
    return map { source ->
        ThreadUtils.execIO(Runnable { run.invoke(source) })
        source
    }
}

/**
 * 执行任务完之后仍然返回当前值
 * @receiver Observable<T>
 * @param run Function1<T, Unit>
 * @return Observable<T>
 */
fun <T> Observable<T>.execute(run: (T) -> Unit): Observable<T>{
    return map {source ->
        run.invoke(source)
        source
    }
}

/**
 * 执行任务完之后仍然返回当前值的Observable
 * @receiver Observable<T>
 * @param run Function1<T, Unit>
 * @return Observable<T>
 */
fun <T,R> Observable<T>.flatExecute(run: (T) -> Observable<R>): Observable<T>{
    return flatMap {source ->
        Log.d("456789","flatExecute")
        run.invoke(source).map { source }
    }
}





