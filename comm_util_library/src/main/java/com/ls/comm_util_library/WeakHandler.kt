package com.ls.comm_util_library

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

/**
 * @ClassName: WeakHandler
 * @Description:
 * @Author: ls
 * @Date: 2020/3/10 18:59
 */
class WeakHandler(t: IHandler): Handler() {
    private val mWeakT = WeakReference<IHandler>(t)

    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        mWeakT.get()?.handleMsg(msg)
    }
}

interface IHandler{
    fun handleMsg(msg: Message?)
}

