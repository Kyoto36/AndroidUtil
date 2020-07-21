package com.ls.comm_util_library

/**
 * @ClassName: ReflectInteractiveFragment2Activity
 * @Description:
 * @Author: ls
 * @Date: 2019/11/6 10:21
 */
abstract class ReflectInteractiveFragment2Activity : IInteractiveFragment2Activity {
    override fun invokeMethod(fragment: IBaseFragment, methodName: String, vararg args: Any?): Boolean {
        if (!ReflectUtils.reflectMethod(getObject(), methodName, *args)) {
            LogUtils.e(javaClass.simpleName, fragment.javaClass.name + " invoke " + getObject() + "." + methodName + " fail !!!")
            return false
        }
        return true
    }

    abstract fun getObject(): Any
}