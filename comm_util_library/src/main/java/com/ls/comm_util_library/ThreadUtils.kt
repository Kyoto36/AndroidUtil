package com.ls.comm_util_library

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

/**
* @FileName: ThreadUtils.kt
* @Description: 线程转换帮助
* @Author: ls
* @Date: 2019/7/22 14:41
*/
class ThreadUtils private constructor() {

    private val mIOThread: ExecutorService = Executors.newCachedThreadPool()
    private val mComputeThread: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val mMainThread: Handler = Handler(Looper.getMainLooper())


    companion object {

        private var sInstance: ThreadUtils? = null

        /**
         * @return 单例对象
         */
        @JvmStatic
        private fun get(): ThreadUtils {
            if (sInstance == null) {
                synchronized(ThreadUtils::class.java) {
                    if (sInstance == null) {
                        sInstance = ThreadUtils()
                    }
                }
            }
            return sInstance!!
        }

        /**
         * 执行在io线程
         * @param run
         */
        @JvmStatic
        fun execIO(run: Runnable) {
            get().mIOThread.submit(run)
        }

        /**
         * 执行在IO线程的可控线程
         * @param call Callable<T>
         * @return Future<T>
         */
        @JvmStatic
        fun <T> execIO(call: Callable<T>): Future<T>{
            return get().mIOThread.submit(call)
        }

        /**
         * 执行在compute线程
         * @param run
         */
        @JvmStatic
        fun execCompute(run: Runnable) {
            get().mComputeThread.submit(run)
        }

        /**
         * 执行在compute线程的可控线程
         * @param call Callable<T>
         * @return Future<T>
         */
        @JvmStatic
        fun <T> execCompute(call: Callable<T>): Future<T>{
            return get().mComputeThread.submit(call)
        }

        /**
         * 执行在main线程
         * @param run
         */
        @JvmStatic
        fun execMain(run: Runnable) {
            if (isMainThread) {
                run.run()
            } else {
                get().mMainThread.post(run)
            }
        }

        /**
         * 延时执行在main线程
         * @param run
         * @param millis 延迟毫秒数
         */
        @JvmStatic
        fun execMain(run: Runnable, millis: Long) {
            get().mMainThread.postDelayed(run, millis)
        }

        @JvmStatic
        val isMainThread: Boolean
            get() = Looper.getMainLooper().thread === Thread.currentThread()
    }


}
