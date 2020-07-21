package com.ls.comm_util_library

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager

/**
 * @ClassName: ImageViewPagerAdapter
 * @Description:
 * @Author: ls
 * @Date: 2019/9/6 23:31
 */ 
abstract class ImageViewPagerAdapter<T>(context: Context,datas: MutableList<T>?,isLoop: Boolean) : ReuseViewPagerAdapter<T>(datas) {
    private var mContext = context

    private var mIsLoop = isLoop


    override fun getItem(position: Int): T {
        return mDatas!![position%mDatas!!.size]
    }

    override fun createView(): View {
        val imageView = ImageView(mContext)
        imageView.layoutParams = ViewPager.LayoutParams()
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return imageView
    }

    override fun getCount(): Int {
        val count = mDatas?.size?:0
        return when {
            count == 0 -> 0
            mIsLoop -> Int.MAX_VALUE
            else -> count
        }
    }

}