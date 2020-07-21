package com.ls.comm_util_library

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Toast

class Util{
    companion object {
        /**
         * Java可调用Util.Companion.sp2px(float)
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun sp2px(dp: Float): Float{
            return (dp * Resources.getSystem().displayMetrics.scaledDensity)
        }

        /**
         * Java可调用Util.Companion.px2sp(int)
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2sp(px: Int): Float{
            return px / Resources.getSystem().displayMetrics.scaledDensity
        }

        /**
         * Java可调用Util.Companion.dp2px(float)
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dp2px(dp: Float): Float{
            return (dp * Resources.getSystem().displayMetrics.density)
        }

        /**
         * Java可调用Util.Companion.px2dp(int)
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2dp(px: Int): Float{
            return px / Resources.getSystem().displayMetrics.density
        }

        /**
         * Java可调用Util.Companion.toast(context,"msg");
         */
        fun toast(context: Context,msg:String){
            context.toast(msg)
        }

        /**
         * Java可调用Util.Companion.setTranslucentStatusBar(getWindow());
         */
        fun setTranslucentStatusBar(window: Window){
            window.setTranslucentStatusBar()
        }

        /**
         * Java可调用Util.Companion.setStatusBarPaddingTopByView(getWindow(),view);
         */
        fun setStatusBarPaddingTopByView(window: Window,view: View){
            window.setStatusBarPaddingTopByView(view)
        }

        /**
         * Java可调用Util.Companion.cancelStatusBarPaddingTopByView(getWindow(),view);
         */
        fun cancelStatusBarPaddingTopByView(window: Window,view: View){
            window.cancelStatusBarPaddingTopByView(view)
        }

        /**
         * Java可调用Util.Companion.changeStatusBarFontDark(getWindow(),view);
         */
        fun changeStatusBarFontDark(window: Window,dark: Boolean){
            window.changeStatusBarFontDark(dark)
        }

        /**
         * Java可调用Util.Companion.setPaddingAndChangeColorByView(getWindow(),view,true);
         */
        fun setStatusPaddingAndChangeColorByView(window: Window, view: View, dark: Boolean){
            window.setStatusPaddingAndChangeColorByView(view, dark)
        }

        /**
         * Java可调用Util.Companion.getDisplayWidth(getWindow());
         */
        fun getDisplayWidth(context: Context): Int{
            return context.getDisplayWidth()
        }

        /**
         * Java可调用Util.Companion.getDisplayHeight(getWindow());
         */
        fun getDisplayHeight(context: Context): Int{
            return context.getDisplayHeight()
        }


        /**
         * Java可调用Util.Companion.getStatusHeight(getWindow());
         */
        fun getStatusHeight(window: Window): Int{
            return window.getStatusHeight()
        }

        /**
         * Java可调用Util.Companion.getNavigationHeight(this);
         */
        fun getNavigationHeight(context: Context): Int{
            return context.getNavigationHeight()
        }

        /**
         * Java可调用Util.Companion.darkenBackground(getWindow(),float)
         * 弹出popupWindow是改变背景的透明度
         */
        fun darkenBackground(window: Window,alpha: Float){
            return window.darkenBackground(alpha)
        }

        /**
         * Java可调用Util.Companion.customDialog(layout,this)
         * 创建自定义dialog
         */
        fun customDialog(layoutId: Int,context: Context) : Dialog{
            var dialog = Dialog(context,R.style.CommDialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(layoutId)
            return dialog
        }

        /**
         * Java可调用Util.Companion.customDialog(view,this)
         * 创建自定义dialog
         */
        fun customDialog(view: View,context: Context) : Dialog{
            var dialog = Dialog(context,R.style.CommDialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(view)
            return dialog
        }

        /**
         * Java可调用Util.Companion.customToast(view,this)
         * 创建自定义Toast
         */
        fun customToast(view:View,context: Context): Toast{
            val toast = Toast(context)
            toast.view = view
            toast.setGravity(Gravity.CENTER,0,0)
            toast.duration = Toast.LENGTH_SHORT
            return toast
        }
    }
}