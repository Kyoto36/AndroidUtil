package com.ls.test.testutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

    int PADDING = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mCaptchaWidth = getWidth() / 5;
//        mCaptchaHeight = getHeight() / 5;
//        Paint paint = new Paint();
//        paint.setColor(Color.GREEN);
//        setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
//
//        float cx=getWidth()/2;
//        float cy=getHeight()/2;
//        float length=cx-PADDING;
//        float a=(float) Math.sqrt(3)*length/2;  //邻边长度
//        Path mPath = new Path();
//        mPath.moveTo(PADDING,cy);
//        //画一个正六边形
//        mPath.lineTo(PADDING+length/2f,cy-a);
//        mPath.lineTo(3/2f*length+PADDING,cy-a);
//        mPath.lineTo(cx+length,cy);
//        mPath.lineTo(3/2f*length+PADDING,cy+a);
//        mPath.lineTo(PADDING+length/2f,cy+a);
//        mPath.lineTo(PADDING,cy);


//        int count = 6;
//        float radius = getWidth() / 2;
//        canvas.translate(radius,radius);
//        for (int i=0;i<count;i++){
//            if (i==0){
//                mPath.moveTo(radius*cos(360/count*i),radius*sin(360/count*i));//绘制起点
//            }else{
//                mPath.lineTo(radius*cos(360/count*i),radius*sin(360/count*i));
//            }
//        }
//        mPath.close();
//        canvas.drawPath(mPath,mPaint);
//
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        Bitmap bitmap1 = Bitmap.createBitmap(getWidth(), getHeight(),Bitmap.Config.ARGB_8888);
//        Canvas temp = new Canvas(bitmap1);
//        Drawable d = getContext().getResources().getDrawable(R.drawable.verification_code_1);
//        d.setBounds(0,0,getWidth(),getHeight());
//        d.draw(temp);
//        canvas.translate(0,0);
//        canvas.drawBitmap(bitmap1,0,0,mPaint);

//        canvas.drawRect(0,0,mCaptchaWidth,mCaptchaHeight,paint);
//        canvas.drawPath(createCaptchaPath(),mPaint);

//        Bitmap bitmap1 = Bitmap.createBitmap(getWidth(), getHeight(),Bitmap.Config.ARGB_8888);
//        Canvas temp = new Canvas(bitmap1);
//        Drawable d = getContext().getResources().getDrawable(R.drawable.duobianxing_touxiang);
//        d.setBounds(0,0,getWidth(),getHeight());
//        d.draw(temp);
//        Bitmap bitmap2 = Bitmap.createBitmap(getWidth(), getHeight(),Bitmap.Config.ARGB_8888);
//        temp = new Canvas(bitmap2);
//        d = getContext().getResources().getDrawable(R.drawable.verification_code_1);
//        d.setBounds(0,0,getWidth(),getHeight());
//        d.draw(temp);
//        canvas.drawBitmap(bitmap1,0,0,paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
//        canvas.drawBitmap(bitmap2,0,0,paint);
//        canvas.drawLine(0F,0F,100F,100F,paint);

//        drawBitmap(canvas);
//        if(mBitmap != null){
//            canvas.drawBitmap(mBitmap,0,0,null);
//        }
        canvas.rotate(30,getWidth()/2,getHeight()/2);
        Path path = getPath(6,getWidth() / 2);
        canvas.drawPath(path,mPaint);
        canvas.rotate(-30,getWidth()/2,getHeight()/2);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(getBitmap(),0,0,mPaint);
        mPaint.setXfermode(null);

//        //设置背景色
//        canvas.drawARGB(255, 139, 197, 186);
//        Paint paint = new Paint();
//        int canvasWidth = canvas.getWidth();
//        int r = canvasWidth / 3;
//        //正常绘制黄色的圆形
//        paint.setColor(0xFFFFCC44);
//        canvas.drawCircle(r, r, r, paint);
//        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        paint.setColor(0xFF66AAFF);
//        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
//        //最后将画笔去除Xfermode
//        paint.setXfermode(null);
    }

    private Bitmap getBitmap(){
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.verification_code_1);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(0, 0, getWidth(), getHeight());

        canvas.drawBitmap(bitmap, rect, rectF, null);
        return result;
    }

    private Path getPath(int num,float radius){
        int count = num;
        Path path = new Path();
        for (int i=0;i<count;i++){
            if (i==0){
                path.moveTo(radius + radius*cos(360/count*i),radius + radius*sin(360/count*i));//绘制起点
            }else{
                path.lineTo(radius + radius*cos(360/count*i),radius + radius*sin(360/count*i));
            }
        }
        path.close();
        return path;
    }

    float sin(int num){
        return (float) Math.sin(num*Math.PI/180);
    }
    float cos(int num){
        return (float) Math.cos(num*Math.PI/180);
    }

    int imageSize, radius;
    Bitmap mBitmap;

    void drawBitmap(Canvas canvas){
        imageSize = getWidth();
        radius = 10;
//        new LoadTask1().execute();
        new LoadTask3().execute();
    }

    class LoadTask1 extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            //这里是获取到原图
            Bitmap bitmapSource = BitmapFactory.decodeResource(getResources(), R.drawable.verification_code_1);
            //我通过getWidth，getHeight获取到了宽高
            Bitmap result = Bitmap.createBitmap(bitmapSource.getWidth(), bitmapSource.getHeight(), Bitmap.Config.ARGB_8888);
            //创建一个画布
            Canvas canvas = new Canvas(result);
            //通过宽高比，获取到最小的那个值
            int min = 0;
            if (result.getWidth() > result.getHeight()){
                min = result.getHeight();
            }else if (result.getWidth() < result.getHeight()){
                min = result.getWidth();
            }else {
                min = result.getWidth();
            }
            canvas.drawCircle(min/2,min/2,min/2,paint);//先画一个圆
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//表示我下一步要取交集的地方
            canvas.drawBitmap(bitmapSource, 0, 0, paint);//又画一个图，并且这个图是在圆形的上面，此时就是获取到交集的地方
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            imageView.setImageBitmap(bitmap);
            mBitmap = bitmap;
            invalidate();
        }
    }

    class LoadTask2 extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap result = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.verification_code_1);

            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(0, 0, imageSize, imageSize);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);


            Path path = new Path();
            path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
            canvas.clipPath(path, Region.Op.INTERSECT);

            canvas.drawBitmap(bitmap, rect, rectF, paint);

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            imageView2.setImageBitmap(bitmap);
            mBitmap = bitmap;
            invalidate();
        }
    }

    class LoadTask3 extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap result = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.verification_code_1);

            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(0, 0, imageSize, imageSize);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);


            Path path = new Path();
//            path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
            int count =6;
            float radius = getWidth() / 2;
//            canvas.translate(radius,radius);

            for (int i=0;i<count;i++){
                if (i==0){
                    path.moveTo(radius + radius*cos(360/count*i),radius + radius*sin(360/count*i));//绘制起点
                }else{
                    path.lineTo(radius + radius*cos(360/count*i),radius + radius*sin(360/count*i));
                }
            }
            path.close();
            canvas.clipPath(path, Region.Op.INTERSECT);
//            canvas.rotate(30);
//            canvas.translate(0,0);
            canvas.drawBitmap(bitmap, rect, rectF, paint);

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            imageView2.setImageBitmap(bitmap);
            mBitmap = bitmap;
            invalidate();
        }
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
