package com.ls.comm_util_library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @ClassName: FragmentUtils
 * @Description:
 * @Author: ls
 * @Date: 2019/8/26 17:03
 */
class FragmentUtils {
    companion object {
        fun <T : Fragment> getFragment(fragmentManager: FragmentManager, fragmentClazz: Class<T>,tag: String = fragmentClazz.name): T {
            var fragment = fragmentManager.findFragmentByTag(tag)
            if (fragment == null) {
                fragment = fragmentClazz.newInstance()
            }
            return fragment!! as T
        }

        fun <T: Fragment> replaceFragment(resId: Int,fragment: T,fragmentManager: FragmentManager,initListener: ((T) -> Unit)): T{
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(resId,fragment)
            initListener.invoke(fragment)
            transaction.commitAllowingStateLoss()
            return fragment
        }

        fun <T: Fragment> removeFragment(fragment: T,fragmentManager: FragmentManager){
            val transaction = fragmentManager.beginTransaction()
            transaction.remove(fragment)
            transaction.commitAllowingStateLoss()
        }

        fun <T : Fragment> switchFragment(resId: Int, currentFragment: T?, fragment: T, fragmentManager: FragmentManager, initListener: ((T) -> Unit)): T {
            return switchFragment(resId, currentFragment, fragment, fragmentManager,0,0,0,0, initListener)
        }

        fun <T: Fragment> showFragment(resId: Int,fragment: T,fragmentManager: FragmentManager,initListener: ((T) -> Unit)): T{
            val transaction = fragmentManager.beginTransaction()
            if (!fragment.isAdded) {
                initListener.invoke(fragment)
                transaction.add(resId, fragment, fragment.javaClass.name).show(fragment).commitAllowingStateLoss()
            } else {
                transaction.show(fragment).commitAllowingStateLoss()
            }
            return fragment
        }

        fun <T: Fragment> hideFragment(fragment: T,fragmentManager: FragmentManager,enterAnim: Int, exitAnim: Int){
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(enterAnim,exitAnim,0,0)
            if (fragment.isAdded) {
                transaction.hide(fragment).commitAllowingStateLoss()
            }
        }

        fun <T : Fragment> switchFragment(resId: Int, currentFragment: T?, fragment: T, fragmentManager: FragmentManager,
                                          enterAnim: Int, exitAnim: Int, backEnterAnim: Int, backExitAnim: Int,
                                          initListener: ((T) -> Unit)): T {
            if (currentFragment !== fragment) {
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(enterAnim,exitAnim,backEnterAnim,backExitAnim)
                if (null != currentFragment) {
                    transaction.hide(currentFragment)
                }
                if (!fragment.isAdded) {
                    initListener.invoke(fragment)
                    transaction.add(resId, fragment, fragment.javaClass.name).show(fragment).commitAllowingStateLoss()
                } else {
                    transaction.show(fragment).commitAllowingStateLoss()
                }
            }
            return fragment
        }
    }
}