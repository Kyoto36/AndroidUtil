package com.ls.custom_view_library.at_edit_text

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.view.MotionEvent

object SpanFactory {
    fun newSpannable(source: CharSequence, vararg spans: Any): Spannable {
        return SpannableString.valueOf(source).apply {
            spans.forEach {
                setSpan(it, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}
