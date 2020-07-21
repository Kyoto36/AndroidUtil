package com.ls.comm_util_library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @ClassName: CommFragmentPagerAdapter
 * @Description:
 * @Author: ls
 * @Date: 2019/11/6 15:33
 */
class CommFragmentPagerAdapter(fragmentManager: FragmentManager,fragments: List<Fragment?>?) : FragmentPagerAdapter(fragmentManager) {

    private var mFragments = fragments

    override fun getItem(position: Int): Fragment {
        return mFragments?.get(position)!!
    }

    override fun getCount(): Int {
        return mFragments?.size?:0
    }
}