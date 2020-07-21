package com.ls.custom_view_library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yufs on 2017/7/4.
 */

public class CircleButtonView extends View{
    private static final int WHAT_LONG_CLICK = 1;
    private Paint mBigCirclePaint;
    private Paint mSmallCirclePaint;
    private Paint mProgressCirclePaint;
    private int mHeight;//当前View的高
    private int mWidth;//当前View的宽
    private float mInitBitRadius;
    private float mInitSmallRadius;
    private float mBigRadius;
    private float mSmallRadius;
    private long mStartTime;
    private long mEndTime;
    private Context mContext;
    private boolean isRecording;//录制状态
    private boolean isMaxTime;//达到最大录制时间
    private float mCurrentProgress;//当前进度

    private long mLongClickTime=150;//长按最短时间(毫秒)，
    private int mTime = 10 * 1000;//录制最大时间s
    private int mMinTime = 800;//录制最短时间
    private int mProgressColor;//进度条颜色
    private float mProgressW=12f;//圆环宽度

    private int mBigCircleColor; // 大圆背景色
    private int mSmallCircleColor; // 小圆背景色

    private boolean isPressed;//当前手指处于按压状态
    private ValueAnimator mProgressAni;//圆弧进度变化

    private boolean mDisableLongClick = false; // 禁用长按

    public CircleButtonView(Context context ) {
        super(context);
        init(context,null);
    }

    public CircleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public CircleButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        this.mContext=context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleButtonView);
        mMinTime=a.getInt(R.styleable.CircleButtonView_minTime,mMinTime);
        mTime=a.getInt(R.styleable.CircleButtonView_maxTime,mTime);
        mProgressW=a.getDimension(R.styleable.CircleButtonView_progressWidth,mProgressW);
        mProgressColor=a.getColor(R.styleable.CircleButtonView_progressColor,Color.parseColor("#6ABF66"));
        mBigCircleColor=a.getColor(R.styleable.CircleButtonView_bigCircleColor,Color.parseColor("#DDDDDD"));
        mSmallCircleColor =a.getColor(R.styleable.CircleButtonView_smallCircleColor,Color.parseColor("#FFFFFF"));
        mDisableLongClick = a.getBoolean(R.styleable.CircleButtonView_disableLongClick,false);
        a.recycle();
        //初始画笔抗锯齿、颜色
        mBigCirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigCirclePaint.setColor(mBigCircleColor);

        mSmallCirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallCirclePaint.setColor(mSmallCircleColor);

        mProgressCirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressCirclePaint.setColor(mProgressColor);

        mProgressAni= ValueAnimator.ofFloat(0, 360f);
        mProgressAni.setDuration(mTime);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mInitBitRadius=mBigRadius= mWidth/2*0.75f;
        mInitSmallRadius=mSmallRadius= mBigRadius*0.75f;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        //绘制外圆
        canvas.drawCircle(mWidth/2,mHeight/2,mBigRadius,mBigCirclePaint);
        //绘制内圆
        canvas.drawCircle(mWidth/2,mHeight/2,mSmallRadius,mSmallCirclePaint);
        //录制的过程中绘制进度条
        if(isRecording){
            drawProgress(canvas);
        }
    }

    /**
     * 绘制圆形进度
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        mProgressCirclePaint.setStrokeWidth(mProgressW);
        mProgressCirclePaint.setStyle(Paint.Style.STROKE);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(mWidth/2-(mBigRadius-mProgressW/2), mHeight/2-(mBigRadius-mProgressW/2), mWidth/2+(mBigRadius-mProgressW/2),mHeight/2+(mBigRadius-mProgressW/2));
        //根据进度画圆弧
        canvas.drawArc(oval, -90, mCurrentProgress, false, mProgressCirclePaint);
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WHAT_LONG_CLICK:
                    //长按事件触发
                    if(onLongClickListener!=null) {
                        onLongClickListener.onLongClick();
                    }
                    startRecordAnimation();
                    break;
            }
        }
    } ;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mDisableLongClick) {
                    isPressed = true;
                    mStartTime = System.currentTimeMillis();
                    Message mMessage = Message.obtain();
                    mMessage.what = WHAT_LONG_CLICK;
                    mHandler.sendMessageDelayed(mMessage, mLongClickTime);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mDisableLongClick) {
                    isPressed = false;
                    isRecording = false;
                    mEndTime = System.currentTimeMillis();
                    if (mEndTime - mStartTime < mLongClickTime) {
                        mHandler.removeMessages(WHAT_LONG_CLICK);
                        if (onClickListener != null)
                            onClickListener.onClick();
                    } else {
                        if (mProgressAni.getCurrentPlayTime() < mMinTime) {
                            startAnimation(mBigRadius, mInitBitRadius, mSmallRadius, mInitSmallRadius);//手指离开时动画复原
                            if (onLongClickListener != null) {
                                onLongClickListener.onNoMinRecord(mMinTime);
                            }
                        } else {
                            //录制完成
                            if (onLongClickListener != null && !isMaxTime) {
                                onLongClickListener.onRecordFinishedListener();
                            }
                            endRecorderAnimation();
                        }
                        if (null != mProgressAni && mProgressAni.isRunning()) {
                            isPressed = false;
                            mCurrentProgress = 0;
                            mProgressAni.cancel();
                        }
                    }
                    break;
                }
                else{
                    if (onClickListener != null)
                        onClickListener.onClick();
                }
        }
        return true;

    }

    private List<ValueAnimator> mReRecorderAnimators = new ArrayList<>();

    public void addReRecorderAnimator(ValueAnimator animator){
        mReRecorderAnimators.add(animator);
    }

    // 重新进入可录制状态
    public void startReRecorder(){
        setVisibility(VISIBLE);
        ValueAnimator alphaAni=ValueAnimator.ofFloat(0,1);
        alphaAni.setDuration(150);
        alphaAni.addUpdateListener(animation -> {
            setAlpha((Float) animation.getAnimatedValue());
        });
        alphaAni.start();
        for (ValueAnimator animator : mReRecorderAnimators){
            animator.start();
        }

    }

    private List<ValueAnimator> mEndRecorderAnimators = new ArrayList<>();

    // 添加录制结束后需要执行的动画
    public void addEndRecorderAnimation(ValueAnimator animator){
        mEndRecorderAnimators.add(animator);
    }

    // 结束录制之后执行的一系列动画
    public void endRecorderAnimation(){
        startAnimation(mBigRadius,mInitBitRadius,mSmallRadius,mInitSmallRadius);//手指离开时动画复原
        ValueAnimator alphaAni = ValueAnimator.ofFloat(1,0);
        alphaAni.setDuration(150);
        alphaAni.addUpdateListener(animation -> {
            setAlpha((Float) animation.getAnimatedValue());
        });
        alphaAni.start();

        for (ValueAnimator animator : mEndRecorderAnimators){
            animator.start();
        }

        alphaAni.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void startRecordAnimation(){
        //内外圆动画，内圆缩小，外圆放大
        startAnimation(mBigRadius,mBigRadius*1.33f,mSmallRadius,mSmallRadius*0.7f);
    }

    private void startAnimation(float bigStart,float bigEnd, float smallStart,float smallEnd) {
        ValueAnimator bigObjAni=ValueAnimator.ofFloat(bigStart,bigEnd);
        bigObjAni.setDuration(150);
        bigObjAni.addUpdateListener(animation -> {
            mBigRadius= (float) animation.getAnimatedValue();
            invalidate();
        });

        ValueAnimator smallObjAni=ValueAnimator.ofFloat(smallStart,smallEnd);
        smallObjAni.setDuration(150);
        smallObjAni.addUpdateListener(animation -> {
            mSmallRadius= (float) animation.getAnimatedValue();
            invalidate();
        });

        bigObjAni.start();
        smallObjAni.start();

        smallObjAni.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRecording=false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //开始绘制圆形进度
                if(isPressed){
                    isRecording=true;
                    isMaxTime=false;
                    startProgressAnimation();
                }
            }



            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    /**
     * 圆形进度变化动画
     */
    private void startProgressAnimation() {
        mProgressAni.start();
        mProgressAni.addUpdateListener(animation -> {
            mCurrentProgress= (float) animation.getAnimatedValue();
            invalidate();
        });

        mProgressAni.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //录制动画结束时，即为录制全部完成
                if(onLongClickListener!=null&&isPressed){
                    isPressed=false;
                    isMaxTime=true;
                    endRecorderAnimation();
                    //录制完成
                    if(onLongClickListener!=null){
                        onLongClickListener.onRecordFinishedListener();
                    }
                    //影藏进度进度条
                    mCurrentProgress=0;
                    invalidate();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 长按监听器
     */
    public interface OnLongClickListener{
        void onLongClick();
        //未达到最小录制时间
        void onNoMinRecord(int currentTime);
        //录制完成
        void onRecordFinishedListener();
    }
    public OnLongClickListener onLongClickListener;

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    /**
     * 点击监听器
     */
    public interface OnClickListener{
        void onClick();
    }
    public OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}