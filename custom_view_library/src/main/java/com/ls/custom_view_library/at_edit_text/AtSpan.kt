package com.ls.custom_view_library.at_edit_text

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

data class AtSpan(val id: String, var name: String, var color: Int = Color.CYAN): DataBindingSpan,
                                                   DirtySpan {

    fun getSpannedName(): Spannable {
        return SpannableString("@$name").apply {
            setSpan(ForegroundColorSpan(color), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun isDirty(text: Spannable): Boolean {
        val spanStart = text.getSpanStart(this)
        val spanEnd = text.getSpanEnd(this)
        return spanStart >= 0 && spanEnd >= 0 && text.substring(spanStart, spanEnd) != "@$name"
    }
}