package com.ls.comm_util_library

import android.text.SpannableStringBuilder
import android.widget.TextView

/**
 * @ClassName: TextViewUtils
 * @Description:
 * @Author: ls
 * @Date: 2020/3/3 17:39
 */
fun calcTextEllipsis(view: TextView, source: SpannableStringBuilder, endSpb: SpannableStringBuilder){
    view.calcTextEllipsis(source, endSpb)
}