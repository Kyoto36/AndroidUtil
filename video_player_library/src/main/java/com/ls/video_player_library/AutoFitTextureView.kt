package com.ls.video_player_library

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.graphics.Paint.DITHER_FLAG
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.ls.comm_util_library.LogUtils
import com.ls.comm_util_library.ThreadUtils
import android.R.attr.bitmap
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.graphics.PorterDuff
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




/**
 * @ClassName: AutoFitTextureView
 * @Description:
 * @Author: ls
 * @Date: 2019/9/3 14:17
 */
class AutoFitTextureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextureView(context, attrs, defStyleAttr) {
    private val TAG = "AutoFitTextureView"
    private var mRatioWidth = 0
    private var mRatioHeight = 0
    private var mPaint: Paint = Paint(ANTI_ALIAS_FLAG or DITHER_FLAG)//画笔
    private var mSrcRect: Rect = Rect()
    private var mDstRect: Rect = Rect()


    fun setAspectRatio(width: Int, height: Int) {
        if (width == mRatioWidth && height == mRatioHeight) {
            return
        }
        Log.e(TAG, "width = $width height = $height")
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    //将图片画到画布上，图片将被以宽度为比例画上去
    fun drawBitmap(bitmap: Bitmap) {
        val canvas = lockCanvas()//锁定画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)// 清空画布
        mSrcRect.set(0, 0, bitmap.width, bitmap.height)//这里我将2个rect抽离出去，防止重复创建
        if (width < height * bitmap.width / bitmap.height) {
            mDstRect.set(0, 0, width, bitmap.height * width / bitmap.width)
        }
        else{
            mDstRect.set(0, 0, height, bitmap.width * height / bitmap.height)
        }
        canvas.drawBitmap(bitmap, mSrcRect, mDstRect, mPaint)//将bitmap画到画布上
        unlockCanvasAndPost(canvas)//解锁画布同时提交
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth)
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height)
            }
        }
    }
}