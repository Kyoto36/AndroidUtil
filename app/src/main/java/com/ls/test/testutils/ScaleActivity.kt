package com.ls.test.testutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ls.glide_library.GlideApp
import kotlinx.android.synthetic.main.activity_scale.*

class ScaleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        GlideApp.with(this).load("/sdcard/Pictures/Screenshots/Screenshot_20181102-120348.jpg").to(scaleImage)
    }
}
