package com.ls.custom_view_library

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * @ClassName: MirrorLayout
 * @Description:
 * @Author: ls
 * @Date: 2019/9/2 10:39
 */
class MirrorLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    override fun dispatchDraw(canvas: Canvas?) {
        if(mIsMirror){
            canvas?.scale(-1F,1F,width/2.0F,height/2.0F)
        }
        super.dispatchDraw(canvas)
    }

    private var mIsMirror = false
    fun setMirror(mirror: Boolean){
        mIsMirror = mirror
    }
}