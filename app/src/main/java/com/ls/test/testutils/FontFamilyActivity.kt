package com.ls.test.testutils

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_font_family.*

class FontFamilyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_family)
//        codeBold.paint.isFakeBoldText = true
        codeBold.paint.style = Paint.Style.FILL_AND_STROKE
        codeBold.paint.strokeWidth = 0.7F
    }
}