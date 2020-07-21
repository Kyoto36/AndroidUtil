package com.ls.comm_util_library

import android.util.AttributeSet
import android.view.View

interface ISpecialViewTask {
    fun execute(view: View,attrs: AttributeSet)
}