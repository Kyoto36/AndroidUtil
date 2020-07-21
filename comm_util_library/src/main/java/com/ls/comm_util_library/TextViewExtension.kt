package com.ls.comm_util_library

import android.os.Build
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

/**
 * @ClassName: EditText
 * @Description:
 * @Author: ls
 * @Date: 2019/11/15 15:27
 */ 

inline fun TextView.addCustomTextChangedListener(
    crossinline afterTextChanged: (edit: Editable?) -> Unit = {},
    crossinline beforeTextChanged: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> },
    crossinline onTextChanged: (text: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> }
){
    addTextChangedListener(object: TextWatcher{
        override fun afterTextChanged(edit: Editable?) = afterTextChanged(edit)

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) = beforeTextChanged(text, start, count, after)

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) = onTextChanged(text, start, before, count)

    })
}

inline fun TextView.doCustomAfterTextChange(crossinline afterTextChanged: (edit: Editable?) -> Unit){
    addCustomTextChangedListener(afterTextChanged = afterTextChanged)
}

inline fun TextView.doCustomBeforeTextChange(crossinline beforeTextChanged: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit){
    addCustomTextChangedListener(beforeTextChanged = beforeTextChanged)
}

inline fun TextView.doCustomOnTextChange(crossinline onTextChanged: (text: CharSequence?, start: Int, before: Int, count: Int) -> Unit){
    addCustomTextChangedListener(onTextChanged = onTextChanged)
}

inline fun TextView.calcTextEllipsis(source: SpannableStringBuilder, endSpb: SpannableStringBuilder) {
    post {
        val newSpb: SpannableStringBuilder
        val size = layout.getEllipsisCount(lineCount - 1)
        if (size > 0) {
            newSpb = source.delete(source.length - size - endSpb.length, source.length)
            newSpb.append(endSpb)
            text = newSpb
        }
    }
}

