package com.ls.custom_view_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.ls.comm_util_library.LogUtils;

public class CircularProgressView extends View {
    public enum Type{
        /**
         * 扇形形式，以扇形进度加载
         */
        SECTOR,
        /**
         * 水翁形式，以水翁进度加载
         */
        RECELINE;
    }
    private Paint mPaint;    //画笔
    private RectF mRectF;    //矩形
    private int mProgress = 0; // 进度
    private Type mType = Type.SECTOR; //进度条形式，默认是扇形
    private int mColor = Color.BLACK;
    private float mAlpha = 0.19F;

    public CircularProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView);
        mColor=a.getColor(R.styleable.CircularProgressView_color,mColor);
        mAlpha=a.getFloat(R.styleable.CircularProgressView_alpha,mAlpha);
        int typeIndex = a.getInt(R.styleable.CircularProgressView_type,0);
        switch (typeIndex){
            default:
            case 0:
                mType = Type.SECTOR;
                break;
            case 1:
                mType = Type.RECELINE;
                break;
        }
        a.recycle();
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        //设置画笔默认颜色
        mPaint.setColor(mColor);
        //设置画笔模式：填充
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(calcAlpha());
        mRectF = new RectF();
    }

    private int calcAlpha(){
        return (int)(mAlpha * 100 * (255 / 100));
    }

    /**
     * 设置进度
     * @param progress 0 - 100
     */
    public void setProgress(@IntRange(from = 0,to = 100) int progress){
        mProgress = progress;
        invalidate();
    }

    /**
     * 设置进度条形式
     * @param type 进度条类型
     */
    public void setType(Type type){
        mType = type;
        invalidate();
    }

    /**
     * 设置遮罩透明度
     * @param alpha 0.0 - 1.0
     */
    public void setShadeAlpha(@FloatRange(from = 0.0,to = 1.0) float alpha){
        mAlpha = alpha;
        mPaint.setAlpha(calcAlpha());
        invalidate();
    }

    /**
     * 设置遮罩颜色
     * @param color
     */
    public void setShadeColor(@ColorInt int color){
        mColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float r = (float) (Math.min(getWidth(), getHeight()) / 2);     //饼状图半径(取宽高里最小的值)
        mRectF.set(-r, -r, r, r);
        int progress = (int) (360 / 100F * mProgress);
        if(mType == Type.RECELINE) {
            int start = 0 - 90 + progress / 2;
            int end = 360 - progress;
            canvas.drawArc(mRectF, start, end, false, mPaint);
        }
        else{
            canvas.drawArc(mRectF, -90, progress - 360, true, mPaint);
        }
    }
}
