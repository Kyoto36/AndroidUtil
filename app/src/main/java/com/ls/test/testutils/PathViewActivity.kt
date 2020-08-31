package com.ls.test.testutils

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.graphics.PathUtils
import com.ls.comm_util_library.PathFactory
import com.ls.comm_util_library.PathImageView
import kotlinx.android.synthetic.main.activity_path_view.*

class PathViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_view)

        pathView.post {
            pathView.apply {
                val config = PathImageView.Config()
                config.setBorderWidth(10F)
                config.setBorderColor(Color.GREEN)
                config.setRotate(30F)
                val radius = if(width > height) (height / 2).toFloat() else (width / 2).toFloat()
                val path = PathFactory.getSquare(6,radius, config.getBorderWidth())
                config.setPath(path)
                setConfig(config)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageResource(R.drawable.verification_code_2)
            }

        }
    }
}