package com.ls.comm_util_library

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi

/**
 * 在targetSDK低于21的时候没办法用context.getColor,这是一个地带方法，当然也只有kotlin可以用
 * @receiver Context
 * @param colorId Int
 * @return Int
 */
fun Context.color(colorId: Int): Int{
    return resources.getColor(colorId)
}

/**
 * sp转px
 * @param dp Float
 * @return Float
 */
fun Any.sp2px(dp: Float): Float{
    return (dp * Resources.getSystem().displayMetrics.scaledDensity)
}

/**
 * px转sp
 * @param px Int
 * @return Float
 */
fun Any.px2sp(px: Int): Float{
    return px / Resources.getSystem().displayMetrics.scaledDensity
}

/**
 * dp转px
 * @param dp Float
 * @return Float
 */
fun Any.dp2px(dp: Float): Float{
    return (dp * Resources.getSystem().displayMetrics.density)
}

/**
 * px转dp
 * @param px Int
 * @return Float
 */
fun Any.px2dp(px: Int): Float{
    return px / Resources.getSystem().displayMetrics.density
}

/**
 * 扩展toast方法
 */
fun Context.toast(msg:String?){
    if(TextUtils.isEmpty(msg)) return
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
 * 如果设置透明状态了并且内容显示到状态栏里，就带上了状态栏的高度
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

@SuppressLint("NewApi")
fun Context.checkDeviceHasNavigationBar(): Boolean {
    //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
    val hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey()
    val hasBackKey: Boolean = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
    return !hasMenuKey && !hasBackKey
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
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            hasNav = getScreenRealHeight() - getStatusHeight() > getDisplayHeight()
        }
        else{
            hasNav = !isNavBarHide()
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
        try {
            val c = Class.forName("android.os.SystemProperties")
            val m = c.getDeclaredMethod("get", String::class.java)
            m.isAccessible = true
            sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String?
        } catch (e: Throwable) {
        }
//        sNavBarOverride = ReflectUtils.reflectStaticMethodResult(Class.forName("android.os.SystemProperties"),"get","qemu.hw.mainkeys") as String?
    }
    return sNavBarOverride
}

/**
 * 是否隐藏了导航键
 *
 * @param context
 * @return
 */
fun Context.isNavBarHide(): Boolean {
    try {
        val brand = Build.BRAND
        // 这里做判断主要是不同的厂商注册的表不一样
        return if (!TextUtils.isEmpty(brand) && (brand.equals("VIVO", ignoreCase = true) || brand.equals("OPPO", ignoreCase = true))) {
            Settings.Secure.getInt(contentResolver, getDeviceForceName(), 0) != 0
        } else if (!TextUtils.isEmpty(brand) && brand.equals("Nokia", ignoreCase = true)) {
            //甚至 nokia 不同版本注册的表不一样， key 还不一样。。。
            (Settings.Secure.getInt(contentResolver, "swipe_up_to_switch_apps_enabled", 0) == 1
                    || Settings.System.getInt(contentResolver, "navigation_bar_can_hiden", 0) != 0)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(contentResolver, getDeviceForceName(), 0) != 0
        } else {
            !checkDeviceHasNavigationBar()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 各个手机厂商注册导航键相关的 key
 *
 * @return
 */
fun getDeviceForceName(): String? {
    val brand = Build.BRAND
    if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"
    return if (brand.equals("HUAWEI", ignoreCase = true) || "HONOR" == brand) {
        "navigationbar_is_min"
    } else if (brand.equals("XIAOMI", ignoreCase = true)) {
        "force_fsg_nav_bar"
    } else if (brand.equals("VIVO", ignoreCase = true)) {
        "navigation_gesture_on"
    } else if (brand.equals("OPPO", ignoreCase = true)) {
        "hide_navigationbar_enable"
    } else if (brand.equals("samsung", ignoreCase = true)) {
        "navigationbar_hide_bar_enabled"
    } else if (brand.equals("Nokia", ignoreCase = true)) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            "navigation_bar_can_hiden"
        } else {
            "swipe_up_to_switch_apps_enabled"
        }
    } else {
        "navigationbar_is_min"
    }
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