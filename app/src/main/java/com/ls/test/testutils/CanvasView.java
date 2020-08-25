package com.ls.test.testutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class CanvasView extends View {

    private Paint mPaint;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCaptchaWidth = getWidth() / 5;
        mCaptchaHeight = getHeight() / 5;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(0,0,mCaptchaWidth,mCaptchaHeight,paint);
        canvas.drawPath(createCaptchaPath(),mPaint);

    }

    int mCaptchaWidth;
    int mCaptchaHeight;

    //生成验证码Path
    private Path createCaptchaPath() {
        Random random = new Random();

        //原本打算随机生成gap，后来发现 宽度/3 效果比较好，
        int widthGap = mCaptchaWidth / 4;
        int heightGap = mCaptchaHeight / 4;

        boolean topCircle = random.nextBoolean();
        boolean leftCircle = random.nextBoolean();
        //随机生成验证码阴影左上角 x y 点，
        int mCaptchaX = leftCircle ? widthGap : 0;
        int mCaptchaY = topCircle ? heightGap : 0;

        Path captchaPath = new Path();
        captchaPath.reset();
        captchaPath.lineTo(0, 0);

        //从左上角开始 绘制一个不规则的阴影
        captchaPath.moveTo(mCaptchaX, mCaptchaY);//左上角
        captchaPath.rLineTo(widthGap, 0);
        //draw 上边的圆
        drawPartCircle(new PointF(mCaptchaX + widthGap, mCaptchaY),
                new PointF(mCaptchaX + widthGap * 2, mCaptchaY),
                captchaPath, topCircle);


        captchaPath.rLineTo(widthGap, 0);//右上角
        captchaPath.rLineTo(0, heightGap);
        //draw 右边的圆
        drawPartCircle(new PointF(mCaptchaX + widthGap * 3, mCaptchaY + heightGap),
                new PointF(mCaptchaX + widthGap * 3, mCaptchaY + heightGap * 2),
                captchaPath, !leftCircle);

        captchaPath.rLineTo(0, heightGap);//右下角
        captchaPath.rLineTo(-widthGap, 0);
        //draw 下边的圆
        drawPartCircle(new PointF(mCaptchaX + widthGap * 2, mCaptchaY + heightGap * 3),
                new PointF(mCaptchaX + widthGap, mCaptchaY + heightGap * 3),
                captchaPath, !topCircle);

        captchaPath.rLineTo(-widthGap, 0);//左下角
        captchaPath.rLineTo(0, -heightGap);
        //draw 左边的圆
        drawPartCircle(new PointF(mCaptchaX, mCaptchaY + heightGap * 2),
                new PointF(mCaptchaX, mCaptchaY + heightGap),
                captchaPath, leftCircle);
        captchaPath.close();
        return captchaPath;
    }

    /**
     * 传入起点、终点 坐标、凹凸和Path。
     * 会自动绘制凹凸的半圆弧
     *
     * @param start 起点坐标
     * @param end   终点坐标
     * @param path  半圆会绘制在这个path上
     * @param outer 是否凸半圆
     */
    public static void drawPartCircle(PointF start, PointF end, Path path, boolean outer) {
        float c = 0.551915024494f;
        //中点
        PointF middle = new PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2);
        //半径
        float r1 = (float) Math.sqrt(Math.pow((middle.x - start.x), 2) + Math.pow((middle.y - start.y), 2));
        //gap值
        float gap1 = r1 * c;

        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            boolean topToBottom = end.y - start.y > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (topToBottom) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸的 两个半圆
                path.cubicTo(start.x + gap1 * flag, start.y,
                        middle.x + r1 * flag, middle.y - gap1 * flag,
                        middle.x + r1 * flag, middle.y);
                path.cubicTo(middle.x + r1 * flag, middle.y + gap1 * flag,
                        end.x + gap1 * flag, end.y,
                        end.x, end.y);
            } else {
                //凹的 两个半圆
                path.cubicTo(start.x - gap1 * flag, start.y,
                        middle.x - r1 * flag, middle.y - gap1 * flag,
                        middle.x - r1 * flag, middle.y);
                path.cubicTo(middle.x - r1 * flag, middle.y + gap1 * flag,
                        end.x - gap1 * flag, end.y,
                        end.x, end.y);
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            boolean leftToRight = end.x - start.x > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (leftToRight) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x, start.y - gap1 * flag,
                        middle.x - gap1 * flag, middle.y - r1 * flag,
                        middle.x, middle.y - r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y - r1 * flag,
                        end.x, end.y - gap1 * flag,
                        end.x, end.y);
            } else {
                //凹 两个半圆
                path.cubicTo(start.x, start.y + gap1 * flag,
                        middle.x - gap1 * flag, middle.y + r1 * flag,
                        middle.x, middle.y + r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y + r1 * flag,
                        end.x, end.y + gap1 * flag,
                        end.x, end.y);
            }


/*
            没推导之前的公式在这里
            if (start.x < end.x) {
                if (outer) {
                    //上左半圆 顺时针
                    path.cubicTo(start.x, start.y - gap1,
                            middle.x - gap1, middle.y - r1,
                            middle.x, middle.y - r1);

                    //上右半圆:顺时针
                    path.cubicTo(middle.x + gap1, middle.y - r1,
                            end.x, end.y - gap1,
                            end.x, end.y);
                } else {
                    //下左半圆 逆时针
                    path.cubicTo(start.x, start.y + gap1,
                            middle.x - gap1, middle.y + r1,
                            middle.x, middle.y + r1);

                    //下右半圆 逆时针
                    path.cubicTo(middle.x + gap1, middle.y + r1,
                            end.x, end.y + gap1,
                            end.x, end.y);
                }
            } else {
                if (outer) {
                    //下右半圆 顺时针
                    path.cubicTo(start.x, start.y + gap1,
                            middle.x + gap1, middle.y + r1,
                            middle.x, middle.y + r1);
                    //下左半圆 顺时针
                    path.cubicTo(middle.x - gap1, middle.y + r1,
                            end.x, end.y + gap1,
                            end.x, end.y);
                }
            }*/
        }
    }
}
