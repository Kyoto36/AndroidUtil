package com.ls.rxjava_library

import io.reactivex.Observable

/**
 * @ClassName: Observables
 * @Description:
 * @Author: ls
 * @Date: 2020/9/14 12:53
 */
class Observables{
    companion object{
        /**
         * 创建Observable
         * @receiver Observable<T>
         * @param provideT Function0<T>
         * @return Observable<T>
         */
        @JvmStatic
        fun <T> create(provideT: () -> T): Observable<T> {
            return Observable.create<T>{
                it.onNext(provideT.invoke())
            }
        }

        @JvmStatic
        fun <T> result(observableT : () -> Observable<T>): Observable<T>{
            return Observable.just(1).flatMap { observableT.invoke() }
        }
    }
}
