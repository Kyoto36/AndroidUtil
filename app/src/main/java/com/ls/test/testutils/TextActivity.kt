package com.ls.test.testutils

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_text.*
import java.util.*

class TextActivity : AppCompatActivity() {
    private val r = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)


        appText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN
                && event.keyCode == KeyEvent.KEYCODE_DEL
            ) {
                appText.setBackgroundColor(
                    Color.rgb(
                        r.nextInt(256), r.nextInt(256), r
                            .nextInt(256)
                    )
                );
            }
            return@setOnKeyListener false
        }

        add.setOnClickListener {
            val str = SpannableString(" @fashdfjkh ")
            str.setSpan(ForegroundColorSpan(Color.RED),0,str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            appText.append(str)
        }
    }
}