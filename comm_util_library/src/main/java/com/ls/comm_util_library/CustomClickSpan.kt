package com.ls.comm_util_library

import android.text.style.ClickableSpan
import android.view.View

/**
 * @ClassName: CustomClickSpan
 * @Description:
 * @Author: ls
 * @Date: 2020/3/24 17:22
 */
class CustomClickSpan(text: String,listener: ISingleListener<String>): ClickableSpan() {
    private val mText = text
    private val mOnClickListener = listener
    override fun onClick(widget: View) {
        mOnClickListener.onValue(mText)
    }
}