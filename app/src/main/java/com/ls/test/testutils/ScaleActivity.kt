package com.ls.test.testutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ls.comm_util_library.changeStatusBarFontDark
import com.ls.comm_util_library.setTranslucentStatusBar
import com.ls.glide_library.GlideApp
import kotlinx.android.synthetic.main.activity_scale.*

class ScaleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        window!!.setTranslucentStatusBar()
        window!!.changeStatusBarFontDark(false)

//        GlideApp.with(this).load("/sdcard/Pictures/Screenshots/Screenshot_20181102-120348.jpg").to(scaleImage)
        GlideApp.with(this).load("/sdcard/Pictures/Screenshots/Screenshot_20181106-182601.jpg").to(scaleImage)
    }
}
