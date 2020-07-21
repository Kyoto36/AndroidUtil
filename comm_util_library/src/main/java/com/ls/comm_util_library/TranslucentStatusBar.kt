package com.ls.comm_util_library

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * 透明状态栏，或者是将内容延伸到状态栏，可配合changeStatusBarColorByView加padding使用
 */
fun Window.setTranslucentStatusBar(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

/**
 * 给提供的View加上一个状态栏高度的paddingTop，(在fragment中使用会有明显的延时，因为使用了post)
 * @param view
 *
 */
fun Window.setStatusBarPaddingTopByView(view : View?){
    if(view == null){
        return
    }
    view.post {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var statusHeight = getStatusHeight()
            view.layoutParams.height = view.height + statusHeight
            view.setPadding(view.paddingLeft, view.paddingTop + statusHeight, view.paddingRight, view.paddingBottom)
            view.requestLayout()
        }
    }
}

/**
 * 给提供的View取消状态栏高度的paddingTop
 */
fun Window.cancelStatusBarPaddingTopByView(view : View?){
    if(view == null){
        return
    }
    view.post {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var statusHeight = getStatusHeight()
            var height = view.paddingTop - statusHeight
            if(height >= 0) {
                view.layoutParams.height = view.layoutParams.height - statusHeight
                view.setPadding(view.paddingLeft, view.paddingTop - statusHeight, view.paddingRight, view.paddingBottom)
                view.requestLayout()
            }
        }
    }
}

fun Window.setStatusPaddingAndChangeColorByView(view : View?, dark: Boolean){
    setStatusBarPaddingTopByView(view)
    changeStatusBarFontDark(dark)
}

fun Window.changeStatusBarFontDark(dark: Boolean) {
    changeStatusBarFontDark_Miui(dark)
    changeStatusBarFontDark_Flyme(dark)
    changeStatusBarFontDark_Original(dark)
}

private fun Window.changeStatusBarFontDark_Original(dark: Boolean) {
    // android6.0+系统
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (dark) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        else{
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
    // 5.0以上6.0以下没有提供改变状态栏字体颜色的方法
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        statusBarColor = 0x55888888
    }
}

private fun Window.changeStatusBarFontDark_Flyme(dark: Boolean) {
    // 魅族FlymeUI
    try {
        val lp = attributes
        val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meizuFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = meizuFlags.getInt(lp)
        value = if (dark) {
            value or bit
        } else {
            value and bit.inv()
        }
        meizuFlags.setInt(lp, value)
        attributes = lp
    } catch (e: Exception) {
//        e.printStackTrace()
    }
}

@SuppressLint("PrivateApi")
private fun Window.changeStatusBarFontDark_Miui(dark: Boolean) {
    // 小米MIUI
    try {
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val darkModeFlag = field.getInt(layoutParams)
        val extraFlagField =
            javaClass.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        if (dark) {    //状态栏亮色且黑色字体
            extraFlagField.invoke(this, darkModeFlag, darkModeFlag)
        } else {       //清除黑色字体
            extraFlagField.invoke(this, 0, darkModeFlag)
        }
    } catch (e: Exception) {
//        e.printStackTrace()
    }
}