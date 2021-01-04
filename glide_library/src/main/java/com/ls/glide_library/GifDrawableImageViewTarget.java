package com.ls.glide_library;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.ls.comm_util_library.IVoidListener;

/**
 * @ClassName: GifDrawableImageTarget
 * @Description:
 * @Author: ls
 * @Date: 2020/10/20 13:17
 */
public class GifDrawableImageViewTarget extends DrawableImageViewTarget {

    private int mPlayCount;
    private IVoidListener mOnPlayEndListener;

    /**
     * @param view
     * @param playCount 播放次数，-1是一直循环
     * @param onPlayEndListener 动效执行结束监听
     */
    public GifDrawableImageViewTarget(ImageView view, int playCount, IVoidListener onPlayEndListener) {
        super(view);
        mPlayCount = playCount;
        mOnPlayEndListener = onPlayEndListener;
    }

    public GifDrawableImageViewTarget(ImageView view, int playCount){
        this(view,playCount,null);
    }

    /**
     * 设置动效执行结束后的监听
     * 这个方法会覆盖掉构造其中传入的监听
     * 用于有些继承此类又需要中转监听，毕竟难受的类初始化流程子类的实例化在父类之后
     * @param listener
     */
    public void setOnPlayEndListener(IVoidListener listener){
        mOnPlayEndListener = listener;
    }

    @Override
    protected void setResource(@Nullable Drawable resource) {
        super.setResource(resource);
        if(resource instanceof GifDrawable){
            GifDrawable gif = (GifDrawable) resource;
            if(mPlayCount > 0){
                gif.setLoopCount(mPlayCount);
            }
            gif.startFromFirstFrame();
            gif.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    if(mOnPlayEndListener != null){
                        mOnPlayEndListener.invoke();
                    }
                }
            });
        }
    }
}
