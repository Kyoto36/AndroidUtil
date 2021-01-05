package com.ls.test.testutils.glideprogress

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.ls.glide_library.progress.ProgressTarget
import com.ls.test.testutils.R
import kotlinx.android.synthetic.main.activity_glide_progress.*

class GlideProgressActivity : AppCompatActivity() {

    private val mUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fclubimg.club.vmall.com%2Fdata%2Fattachment%2Fforum%2F202007%2F17%2F232123momgketgdekbqtvl.jpg&refer=http%3A%2F%2Fclubimg.club.vmall.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612328013&t=a4c3948df847f33d3f712c655019799b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_progress)

        load.setOnClickListener {
            Glide.with(this).load(mUrl).into(object : ProgressTarget<Drawable>(mUrl,DrawableImageViewTarget(imageView)){
                override fun onProgress(ppm: Float) {
                    progress.post {
                        if (progress.visibility == View.GONE) {
                            progress.visibility = View.VISIBLE
                        }
                        progress.setProgress((ppm / 100).toInt())
                    }
                }

                override fun onEnd() {
                    progress.post {
                        if (progress.visibility == View.VISIBLE) {
                            progress.visibility = View.GONE
                        }
                    }
                }

            })
        }


    }
}