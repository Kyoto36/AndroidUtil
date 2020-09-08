package com.ls.comm_util_library

import android.animation.Animator
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.*

/**
 * @ClassName: AnimUtils
 * @Description:
 * @Author: ls
 * @Date: 2020/9/7 10:46
 */
object AnimUtils {
    fun startShakeCentreByViewAnim(view: View?, scaleSmall: Float, scaleLarge: Float, shakeDegrees: Float, duration: Long) {
        if (view == null) {
            return
        }
        //TODO 验证参数的有效性

        //由小变大
        val scaleAnim: Animation = ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge)
        //从左向右
        val rotateAnim: Animation = RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnim.setDuration(duration)
        rotateAnim.setDuration(duration / 10)
        rotateAnim.setRepeatMode(Animation.REVERSE)
        rotateAnim.setRepeatCount(10)
        val smallAnimationSet = AnimationSet(false)
        smallAnimationSet.addAnimation(scaleAnim)
        smallAnimationSet.addAnimation(rotateAnim)
        view.startAnimation(smallAnimationSet)
    }

    fun startShakeCentreByPropertyAnim(view: View?, scaleSmall: Float, scaleLarge: Float, shakeDegrees: Float, duration: Long) {
        if (view == null) {
            return
        }
        //TODO 验证参数的有效性

        //先变小后变大
        val scaleXValuesHolder: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        )
        val scaleYValuesHolder: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        )

        //先往左再往右
        val rotateValuesHolder: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        )
        val objectAnimator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder)
        objectAnimator.setDuration(duration)
        objectAnimator.start()
    }

    fun startShakeHorizontalByPropertyAnim(view: View?,range: Float,cycles: Float,duration: Long){
        if(view == null) return
        val anim = view.animate().translationX(range)
                .setInterpolator(CycleInterpolator(cycles))
                .setDuration(duration)
        anim.setListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                view.translationX = 0F
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    fun startShakeHorizontalByViewAnim(view: View?,range: Float,cycles: Float,duration: Long){
        if(view == null) return
//        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF,-range,Animation.RELATIVE_TO_SELF,range,Animation.RELATIVE_TO_SELF,0F,Animation.RELATIVE_TO_SELF,0F)
        val translateAnimation = TranslateAnimation(-range,range,0F,0F)
        translateAnimation.repeatCount = cycles.toInt()
        translateAnimation.repeatMode = Animation.REVERSE
        translateAnimation.duration = (duration / cycles).toLong()
        translateAnimation.interpolator = CycleInterpolator(cycles)
        view.startAnimation(translateAnimation)
    }
}