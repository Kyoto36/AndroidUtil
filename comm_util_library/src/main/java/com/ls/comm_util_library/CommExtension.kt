package com.ls.comm_util_library

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.ViewConfiguration
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.reflect.Method


/**
 * 扩展toast方法
 */
fun Context.toast(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}

/**
 * 获取显示宽度
 * @return 返回像素px
 */
fun Context.getDisplayWidth():Int {
    val dm = getDisplayMetrics()
    return dm.widthPixels
}

/**
 * 获取显示高度，不带状态栏和导航栏
 * @return 返回像素px
 */
fun Context.getDisplayHeight(): Int {
    val dm = getDisplayMetrics()
    return dm.heightPixels
}

/**
 * 获取屏幕宽度
 * @return 返回像素px
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.getScreenRealWidth(): Int{
    val dm = getDisplayRealMetrics()
    return dm.widthPixels
}

/**
 * 获取屏幕高度，带状态栏和导航栏
 * @return 返回像素px
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Context.getScreenRealHeight(): Int{
    val dm = getDisplayRealMetrics()
    return dm.heightPixels
}

fun Window.darkenBackground(alpha: Float){
    attributes.alpha = alpha
    addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
}

/**
 * 获取状态栏高度
 * @return 返回dp
 */
fun Window.getStatusHeight(): Int {
    var result = 0
    // 通过WindowMangerService获取，在未显示的时候可能获取到0
    val rectangle = Rect()
    decorView.getWindowVisibleDisplayFrame(rectangle)
    result = rectangle.top
    if(result == 0){
        result = decorView.context.getStatusHeight()
    }
    return result
}

fun Context.getStatusHeight(): Int{
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    if(result == 0){
        val clazz = ReflectUtils.getClass("com.android.internal.R${"$"}dimen")
        if(clazz != null){
            val obj = clazz.newInstance()
            val field = clazz.getField("status_bar_height")
            result = resources.getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()))
        }
    }
    if(result == 0){
        result = Util.dp2px(20.0F).toInt()
    }
    return result
}

/**
 * 获取导航栏高度
 * @return 返回dp
 */
fun Context.getNavigationHeight(): Int {
    // 通过WindowMangerService获取，在未显示的时候可能获取到0
    var result = 0
    if (hasNavBar()) {
        var resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
    }
    LogUtils.e("result = ", result.toString())
    return result
}

fun Context.hasNavBar(): Boolean{
    val resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return if (resourceId != 0) {
        var hasNav = resources.getBoolean(resourceId)
        // check override flag
        val sNavBarOverride = getNavBarOverride()
        if ("1" == sNavBarOverride) {
            hasNav = false
        } else if ("0" == sNavBarOverride) {
            hasNav = true
        }
        hasNav
    } else { // fallback
        !ViewConfiguration.get(this).hasPermanentMenuKey()
    }
}

@SuppressLint("PrivateApi")
fun getNavBarOverride(): String?{
    var sNavBarOverride: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        try {
//            sNavBarOverride = System.getProperty("qemu.hw.mainkeys")
//            val c = Class.forName("android.os.SystemProperties")
//            val m = c.getDeclaredMethod("get", String::class.java)
//            m.isAccessible = true
//            sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String?
//        } catch (e: Throwable) {
//        }
        sNavBarOverride = ReflectUtils.reflectStaticMethodResult(Class.forName("android.os.SystemProperties"),"get","qemu.hw.mainkeys") as String?
    }
    return sNavBarOverride
}

private fun Context.getDisplayMetrics(): DisplayMetrics {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    return dm
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun Context.getDisplayRealMetrics(): DisplayMetrics{
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getRealMetrics(dm)
    return dm
}