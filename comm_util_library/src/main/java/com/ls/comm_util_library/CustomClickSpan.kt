package com.ls.comm_util_library

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * @ClassName: CustomClickSpan
 * @Description:
 * @Author: ls
 * @Date: 2020/3/24 17:22
 */
class CustomClickSpan: ClickableSpan {

    private var mColor = -1
    private var mUnderline = true
    private val mText: CharSequence
    private val mOnClickListener: ISingleListener<CharSequence>?
    private val mOnClickListener2: View.OnClickListener?

    constructor(text: CharSequence,listener: ISingleListener<CharSequence>?):
            this(text,-1,true,listener,null)

    constructor(text: CharSequence,color: Int,underline: Boolean,listener: ISingleListener<CharSequence>?):
            this(text,color,underline,listener,null){}

    constructor(text: CharSequence,color: Int,underline: Boolean,listener2: View.OnClickListener?):
            this(text,color,underline,null,listener2){}

    private constructor(text: CharSequence,color: Int,underline: Boolean,listener: ISingleListener<CharSequence>?,listener2: View.OnClickListener?): super(){
        mText = text
        mColor = color
        mUnderline = underline
        mOnClickListener = listener
        mOnClickListener2 = listener2
    }

    override fun onClick(widget: View) {
        mOnClickListener?.onValue(mText)
        mOnClickListener2?.onClick(widget)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        if(mColor != -1) {
            //设置颜色
            ds.color = mColor
            ds.linkColor = mColor
        }
        //设置是否要下划线
        ds.isUnderlineText = mUnderline
    }
}