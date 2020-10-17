package com.ls.test.testutils.intensify

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.ls.test.testutils.R
import kotlinx.android.synthetic.main.activity_intensify.*
import java.io.IOException


class IntensifyActivity : AppCompatActivity() {

    private val mPictures by lazy {
        assets.list("pic")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intensify)
        viewPager.adapter = ImagePageAdapter()
    }

    inner class ImagePageAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mPictures?.size?:0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = IntensifyImageView(container.context)
            imageView.scaleType = IntensifyImage.ScaleType.CENTER_INSIDE
            try {
                imageView.setImage(
                    assets.open(
                        "pic/" + mPictures?.get(
                            position
                        )
                    )
                )
            } catch (e: IOException) {
                Log.w("123123", e)
            }
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: View, position: Int, `object`: Any) {
            (container as ViewGroup).removeView(`object` as View?)
        }
    }
}