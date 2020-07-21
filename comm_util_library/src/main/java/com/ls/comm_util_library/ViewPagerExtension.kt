package com.ls.comm_util_library

import androidx.annotation.Px
import androidx.viewpager.widget.ViewPager

/**
 * @ClassName: ViewPagerExtension
 * @Description:
 * @Author: ls
 * @Date: 2019/12/10 16:29
 */
inline fun ViewPager.addOnPageChangeListener(
        crossinline onPageScrollStateChanged: (state: Int) -> Unit = {},
        crossinline onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = {position: Int, positionOffset: Float, positionOffsetPixels: Int ->},
        crossinline onPageSelected: (position: Int) -> Unit = {}
){
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) = onPageScrollStateChanged(state)

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = onPageScrolled(position,positionOffset,positionOffsetPixels)

        override fun onPageSelected(position: Int) = onPageSelected(position)
    })
}

inline fun ViewPager.onPageScrollStateChanged(crossinline onPageScrollStateChanged: (state: Int) -> Unit):ViewPager{
    addOnPageChangeListener(onPageScrollStateChanged = onPageScrollStateChanged)
    return this
}

inline fun ViewPager.onPageScrolled(crossinline onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit): ViewPager{
    addOnPageChangeListener(onPageScrolled = onPageScrolled)
    return this
}

inline fun ViewPager.onPageSelected(crossinline onPageSelected: (position: Int) -> Unit): ViewPager{
    addOnPageChangeListener(onPageSelected = onPageSelected)
    return this
}

