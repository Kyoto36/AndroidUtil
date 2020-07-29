package com.ls.custom_view_library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressView extends View {
    private Paint mPaint;    //画笔
    private RectF mRectF;    //矩形
    public CircularProgressView(Context context) {
        super(context);
        initPaint();
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        //设置画笔默认颜色
        mPaint.setColor(Color.BLUE);
        //设置画笔模式：填充
        mPaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float r = (float) (Math.min(getWidth(), getHeight()) / 2);     //饼状图半径(取宽高里最小的值)
        mRectF.set(-r, -r, r, r);
        canvas.drawArc(mRectF, 0, 90, true, mPaint);
    }
}
