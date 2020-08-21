package com.ls.custom_view_library.puzzle_verify;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.ls.comm_util_library.Util;

public class TextSeekBar extends AppCompatSeekBar {

    private Paint mTextPaint;

    public TextSeekBar(Context context) {
        this(context,null);
    }

    public TextSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        int textSize = (int)Util.Companion.dp2px(14);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#545454"));
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (getHeight() / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText("向右滑动滑块完成拼图", getWidth() / 2, baseLineY, mTextPaint);
    }
}
