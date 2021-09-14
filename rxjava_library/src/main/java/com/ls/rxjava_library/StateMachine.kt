package com.ls.rxjava_library

import android.util.SparseArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import androidx.core.util.set
import com.ls.comm_util_library.DoubleData
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * @ClassName: StateMachine
 * @Description:
 * @Author: ls
 * @Date: 2020/9/9 12:45
 */
class StateMachine {
    private val mCurrentState = SparseArray<DoubleData<Int, Long>>()
    private val mReadWriteLock = ReentrantReadWriteLock()

    fun updateState(index: Int,status: Int): Long{
        val time = System.currentTimeMillis()
        mReadWriteLock.writeLock().lock()
        try {
            mCurrentState.put(index, DoubleData(status,time))
        } finally {
            mReadWriteLock.writeLock().unlock()
        }
        return time
    }

    fun check(index: Int,status: Int,time: Long): Boolean{
        var result = false
        mReadWriteLock.readLock().lock()
        try {
            val state = mCurrentState[index]
            result = (state.t == status) && (state.s == time)
        } finally {
            mReadWriteLock.readLock().unlock()
        }
        return result
    }
}