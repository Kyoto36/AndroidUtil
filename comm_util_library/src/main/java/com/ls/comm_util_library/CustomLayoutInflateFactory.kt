package com.ls.comm_util_library

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

/**
 * @FileName: CustomLayoutInflateFactory.kt
 * @Description: 自定义的view创建工厂，一般用于动态改变app主题
 * @Author: ls
 * @Date: 2019/7/22 14:42
 */
class CustomLayoutInflateFactory : LayoutInflater.Factory2 {

    private var mSpecialViewTask: List<ISpecialViewTask>? = null

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        val inflater = LayoutInflater.from(context)
        var view: View? = null

//        LogUtils.d(this, "onCreateView view name = $name")
        if (-1 == name.indexOf('.')) {
            try {
                view = inflater.createView(name, "android.view.", attrs)
            } catch (e: Exception) {
            }
            if (view == null) {
                try {
                    view = inflater.createView(name, "android.widget.", attrs)
                } catch (e: Exception) {
                }
            }
            if (view == null) {
                try {
                    view = inflater.createView(name, "android.webkit.", attrs)
                } catch (e: Exception) {
                }
            }
        } else {
            try {
                view = inflater.createView(name, null, attrs)
            } catch (e: Exception) {
            }
        }

        if(view == null) {
            LogUtils.e(javaClass.simpleName, "onCreateView create $name fail !!!")
        }

        if (null != view && null != mSpecialViewTask) {
            for (i in mSpecialViewTask!!.indices) {
                mSpecialViewTask!![i].execute(view, attrs)
            }
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }

    class Builder {
        private var specialViewTask: MutableList<ISpecialViewTask>? = null

        fun addTask(task: ISpecialViewTask):Builder {
            if (specialViewTask == null) {
                specialViewTask = ArrayList()
            }
            if (!specialViewTask!!.contains(task)) {
                specialViewTask!!.add(task)
            }
            return this
        }

        fun build(): CustomLayoutInflateFactory {
            return CustomLayoutInflateFactory().run {
                mSpecialViewTask = specialViewTask
                this
            }
        }
    }
}


