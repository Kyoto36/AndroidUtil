package com.ls.rxjava_library

import android.util.SparseArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import androidx.core.util.set
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
    private val mCurrentState = SparseIntArray()
    private val mStateNewest = SparseArray<Long>()
    private val mReadWriteLock = ReentrantReadWriteLock()

    fun updateState(index: Int,status: Int): Long{
        val time = System.currentTimeMillis()
        mReadWriteLock.writeLock().lock()
        try {
            mCurrentState[index] = status
            mStateNewest[status] = time
        } finally {
            mReadWriteLock.writeLock().unlock()
        }
        return time
    }

    fun check(index: Int,status: Int,time: Long): Boolean{
        var result = false
        mReadWriteLock.readLock().lock()
        try {
            if(mCurrentState[index] == status){
                result = mStateNewest[status] == time
            }
        } finally {
            mReadWriteLock.readLock().unlock()
        }
        return result
    }
}