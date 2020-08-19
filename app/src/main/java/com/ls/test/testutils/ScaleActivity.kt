package com.ls.test.testutils

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.ls.comm_util_library.LogUtils
import com.ls.comm_util_library.changeStatusBarFontDark
import com.ls.comm_util_library.getDisplayHeight
import com.ls.comm_util_library.setTranslucentStatusBar
import com.ls.custom_view_library.ScaleImageView
import com.ls.glide_library.GlideApp
import kotlinx.android.synthetic.main.activity_scale.*

class ScaleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        window!!.setTranslucentStatusBar()
        window!!.changeStatusBarFontDark(false)

//        GlideApp.with(this).load("/sdcard/Pictures/Screenshots/Screenshot_20181102-120348.jpg").to(scaleImage)
        GlideApp.with(this).load("/sdcard/Pictures/Screenshots/Screenshot_20181106-182601.jpg").to(scaleImage)
        scaleImage.setOnClickListener {
            finish()
        }
        rootView.background.alpha = 255
        scaleImage.setOnDownSlideListener(object : ScaleImageView.IOnDownSlideListener{
            override fun onStart() {
                text.visibility = View.GONE
                LogUtils.e("bbbbbb","onStart")
            }

            override fun onScroll(distance: Float) {
                scaleImage.translationY += distance
                var transRatio = Math.abs(scaleImage.translationY) / (getDisplayHeight() / 3)
                transRatio = if(transRatio < 0) 0F else if(transRatio > 1) 1F else transRatio
                rootView.background.alpha = 255 - (255 * transRatio).toInt()
                LogUtils.e("bbbbbb","onScroll distance $distance")
            }

            override fun onEnd(cancel: Boolean,velocity: Float) {
                if(!cancel && Math.abs(velocity) > 800 || Math.abs(scaleImage.translationY) > getDisplayHeight() / 3){
                    animatorFinish()
                }
                else {
                    text.visibility = View.VISIBLE
                    animatorReset()
                }
                LogUtils.e("bbbbbb","onEnd velocity $velocity scaleImage.translationY ${scaleImage.translationY}")
            }
        })
    }

    private fun animatorFinish(){
        val alreadyY = scaleImage.translationY
        if(alreadyY == 0F) return
        var needY = getDisplayHeight() - Math.abs(alreadyY)
        if(alreadyY < 0) needY = -needY
        val alreadyAlpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rootView.background.alpha
        } else {
            255
        }
        val animator = ValueAnimator.ofFloat(0F,1F)
        animator.duration = 300
        animator.addUpdateListener {
            scaleImage.translationY = alreadyY + needY * (it.animatedValue as Float)
            rootView.background.alpha = (alreadyAlpha - alreadyAlpha * (it.animatedValue as Float)).toInt()
        }
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                finish()
                overridePendingTransition(0,0)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        animator.start()
    }

    private fun animatorReset(){
        val alreadyY = scaleImage.translationY
        if(alreadyY == 0F) return
        val alreadyAlpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rootView.background.alpha
        } else {
            255
        }
        val needAlpha = 255 - alreadyAlpha
        val translationY = scaleImage.translationY
        val animator = ValueAnimator.ofFloat(0F,1F)
        animator.duration = 300
        animator.addUpdateListener {
            scaleImage.translationY = alreadyY - alreadyY * (it.animatedValue as Float)
            rootView.background.alpha = (alreadyAlpha + needAlpha * (it.animatedValue as Float)).toInt()
        }
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        animator.start()
    }
}
