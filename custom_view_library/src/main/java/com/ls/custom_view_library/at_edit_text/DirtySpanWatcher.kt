package com.ls.custom_view_library.at_edit_text

import android.text.Spannable

class DirtySpanWatcher(private val removePredicate: (Any) -> Boolean) : SpanWatcherAdapter() {

    override fun onSpanChanged(text: Spannable, what: Any, ostart: Int, oend: Int, nstart: Int,
                               nend: Int) {
        if (what is DirtySpan && what.isDirty(text)) {
            val spanStart = text.getSpanStart(what)
            val spanEnd = text.getSpanEnd(what)
            text.getSpans(spanStart, spanEnd, Any::class.java).filter {
                removePredicate.invoke(it)
            }.forEach {
                text.removeSpan(it)
            }
        }
    }
}