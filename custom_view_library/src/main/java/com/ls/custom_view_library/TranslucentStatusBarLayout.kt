package com.ls.custom_view_library

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import com.ls.comm_util_library.LogUtils
import com.ls.comm_util_library.changeStatusBarFontDark
import com.ls.comm_util_library.getStatusHeight
import com.ls.comm_util_library.setTranslucentStatusBar
import java.lang.IllegalStateException

class TranslucentStatusBarLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){
    private var mStatusView: View
    init {
        orientation = VERTICAL
        mStatusView = View(context)
        val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0)
        addView(mStatusView,0,lp)
    }

    private var mWindow: Window? = null
    fun attach(window: Window){
        mWindow = window
    }

    fun attachAndTranslucent(window: Window,isDark: Boolean){
        attach(window)
        translucentStatusBar()
        changeStatusTextColor(isDark)
    }

    fun translucentStatusBar(){
        if(mWindow == null) return
        mWindow!!.setTranslucentStatusBar()
        val statusHeight = mWindow!!.getStatusHeight()
        mStatusView.layoutParams.height = statusHeight
        requestLayout()
    }

    fun changeStatusTextColor(isDark: Boolean){
        if(mWindow == null) return
        mWindow!!.changeStatusBarFontDark(isDark)
    }
}
