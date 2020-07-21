package com.ls.dagger_library

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

abstract class InjectPager {

    protected var mFragment: Fragment? = null
    protected var mActivity: FragmentActivity? = null

    @Inject
    @JvmField
    protected var mViewModelFactory: ViewModelFactory? = null

    constructor(activity: FragmentActivity){
        attach(null,activity)
    }

    constructor(fragment: Fragment){
        attach(fragment,fragment.activity)
    }

    fun getActivity(): Activity?{
        return mActivity
    }

    fun getFragment(): Fragment?{
        return mFragment
    }

    fun attach(fragment: Fragment?, activity: FragmentActivity?) {
        mFragment = fragment
        mActivity = activity
        CustomInjection.inject(this)
    }
}