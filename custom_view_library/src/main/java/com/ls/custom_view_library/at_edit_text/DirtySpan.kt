package com.ls.custom_view_library.at_edit_text

import android.text.Spannable

interface DirtySpan {
    fun isDirty(text: Spannable): Boolean
}