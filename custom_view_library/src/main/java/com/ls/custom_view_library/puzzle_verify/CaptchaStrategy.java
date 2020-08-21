package com.ls.custom_view_library.puzzle_verify;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.ls.comm_util_library.Size;

/**
 * Captcha的拼图区域策咯
 * Created by luozhanming on 2018/1/19.
 */

public abstract class CaptchaStrategy {

    protected Context mContext;

    public CaptchaStrategy(Context context) {
        this.mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    public abstract Size getBlockSize(int viewWidth, int viewHeight);

    /**
     * 定义缺块的形状
     *
     * @param blockSize 单位dp，注意转化为px
     * @return path of the shape
     */
    public abstract Path getBlockShape(Size blockSize);

    /**
     * 定义缺块的位置信息
     *
     * @param width  picture width unit:px
     * @param height picture height unit:px
     * @param blockSize
     * @return position info of the block
     */
    public abstract PositionInfo getBlockPositionInfo(int width, int height, Size blockSize);

    /**
     * 定义滑块图片的位置信息(只有设置为无滑动条模式有用)
     *
     * @param width  picture width
     * @param height picture height
     * @return position info of the block
     */
    public PositionInfo getPositionInfoForSwipeBlock(int width, int height, Size blockSize){
        return getBlockPositionInfo(width,height,blockSize);
    }

    /**
     * 获得缺块阴影的Paint
     */
    public abstract Paint getBlockShadowPaint();

    /**
     * 获得滑块图片的Paint
     */
    public abstract Paint getBlockBitmapPaint();

    /**
     * 装饰滑块图片，在绘制图片后执行，即绘制滑块前景
     */
    public void decoreateSwipeBlockBitmap(Canvas canvas, Path shape) {

    }
}
