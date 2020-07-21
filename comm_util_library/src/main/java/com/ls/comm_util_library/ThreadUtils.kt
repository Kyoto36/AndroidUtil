package com.ls.comm_util_library

import android.os.Handler
import android.os.Looper

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

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
        fun execIO(run: Runnable) {
            get().mIOThread.submit(run)
        }

        /**
         * 执行在compute线程
         * @param run
         */
        fun execCompute(run: Runnable) {
            get().mComputeThread.submit(run)
        }

        /**
         * 执行在main线程
         * @param run
         */
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
        fun execMain(run: Runnable, millis: Long) {
            get().mMainThread.postDelayed(run, millis)
        }

        val isMainThread: Boolean
            get() = Looper.getMainLooper().thread === Thread.currentThread()
    }


}
