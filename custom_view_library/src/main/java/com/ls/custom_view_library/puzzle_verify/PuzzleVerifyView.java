package com.ls.custom_view_library.puzzle_verify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.ls.comm_util_library.IStateListener;
import com.ls.comm_util_library.Size;

import java.util.Random;

public class PuzzleVerifyView extends AppCompatImageView {

    enum State{
        ACCESS,UNACCESS,MOVEING
    }

    enum Mode{
        /**
         * 带滑动条验证模式
         */
        MODE_BAR,
        /**
         * 不带滑动条验证，手触模式
         */
        MODE_NONBAR,
    }



    private State mState = State.RESET;
    private long mStartTime = 0;
    private IStateListener<Void> mAccessListener;
    private Size mBlockSize;
    private Bitmap mMaskBitmap;
    private Bitmap mDisplayBitmap;
    private Paint mBlockShadowPaint;
    private Path mBlockMaskPath;
    private PositionInfo mMaskPosition;
    private PositionInfo mBlockPosition;
    private CaptchaStrategy mCaptchaStrategy;
    private float tempX, tempY, downX, downY;

    private Mode mMode = Mode.MODE_BAR;


    public PuzzleVerifyView(Context context) {
        this(context,null);
    }

    public PuzzleVerifyView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PuzzleVerifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mCaptchaStrategy = new DefaultCaptchaStrategy(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBlock();
        if(mState != State.ACCESS) {
            canvas.drawPath(mBlockMaskPath, mBlockShadowPaint);
            canvas.drawBitmap(mMaskBitmap, mMaskPosition.left, mMaskPosition.top, new Paint());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMode == Mode.MODE_NONBAR && mMaskBitmap != null) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = x;
                    downY = y;
                    startByTouch(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    checkAccess();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float offsetX = x - tempX;
                    float offsetY = y - tempY;
                    moveByTouch(offsetX, offsetY);
                    break;
            }
            tempX = x;
            tempY = y;
        }
        return true;
    }

    public long getStartTime(){
        return mStartTime;
    }

    public void move(int progress){
        if(mState == State.RESET) return;
        mState = State.MOVEING;
        mMaskPosition.left = (int)(progress / 100f * (getWidth() - mBlockSize.getWidth()));
        invalidate();
    }

    /**
     * 触动拼图缺块(触动模式)
     */
    public void moveByTouch(float offsetX, float offsetY) {
        if(mState == State.RESET) return;
        mState = State.MOVEING;
        mMaskPosition.left += offsetX;
        mMaskPosition.top += offsetY;
        invalidate();
    }

    /**
     *触动拼图块(触动模式)
     * */
    void startByTouch(float x, float y) {
        if(mState == State.RESET) invalidate();
        mStartTime = System.currentTimeMillis();
        mMaskPosition.left = (int) (x - mBlockSize.getWidth() / 2f);
        mMaskPosition.top = (int) (y - mBlockSize.getHeight() / 2f);
        invalidate();
    }

    public void start(){
        mStartTime = System.currentTimeMillis();
    }

    public void stop(){
        checkAccess();
    }

    public void setMode(Mode mode){
        mMode = mode;
    }

    private static final int TOLERANCE = 10;         //验证的最大容差

    /**
     * 检测是否通过
     */
    public void checkAccess() {
        if (Math.abs(mBlockPosition.left - mMaskPosition.left) < TOLERANCE && Math.abs(mBlockPosition.top - mMaskPosition.top) < TOLERANCE) {
            mState = State.ACCESS;
            invalidate();
            if(mAccessListener != null){
                mAccessListener.onSuccess(null);
            }
        } else {
            if(mAccessListener != null){
                mAccessListener.onFailed(null);
            }
        }
    }

    public void setAccessListener(IStateListener<Void> listener){
        mAccessListener = listener;
    }

    public void reset(){
        mState = State.RESET;
        mBlockSize = null;
        mBlockPosition = null;
        mMaskPosition = null;
        mBlockShadowPaint = null;
        mBlockMaskPath = null;
        mDisplayBitmap = null;
        mMaskBitmap = null;
    }

    private void initBlock(){
        if(mBlockSize == null) {
            mBlockSize = mCaptchaStrategy.getBlockSize(getWidth(),getHeight());
        }
        if(mBlockPosition == null){
            mBlockPosition = mCaptchaStrategy.getBlockPositionInfo(getWidth(),getHeight(),mBlockSize);
        }
        if(mMaskPosition == null){
            if (mMode == Mode.MODE_BAR) {
                mMaskPosition = new PositionInfo(0, mBlockPosition.top);
            } else {
                mMaskPosition = mCaptchaStrategy.getPositionInfoForSwipeBlock(getWidth(), getHeight(), mBlockSize);
            }
        }
        if(mBlockShadowPaint == null){
            mBlockShadowPaint = mCaptchaStrategy.getBlockShadowPaint();
        }
        if(mBlockMaskPath == null){
            mBlockMaskPath = mCaptchaStrategy.getBlockShape(mBlockSize);
            mBlockMaskPath.offset(mBlockPosition.left,mBlockPosition.top);
        }
        if(mDisplayBitmap == null){
            mDisplayBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(mDisplayBitmap);
            tempCanvas.concat(getImageMatrix());
            getDrawable().draw(tempCanvas);
        }
        if(mMaskBitmap == null){
            Bitmap tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.clipPath(mBlockMaskPath);
            tempCanvas.drawBitmap(mDisplayBitmap,0,0,new Paint());
            mMaskBitmap = Bitmap.createBitmap(tempBitmap, mBlockPosition.left, mBlockPosition.top, mBlockSize.getWidth(), mBlockSize.getHeight());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private Rect getImgDisplaySize() {
        Rect rect = new Rect();
        if (getDrawable() != null) {
            //获得ImageView中Image的真实宽高，
            int dw = getDrawable().getBounds().width();
            int dh = getDrawable().getBounds().height();

            //获得ImageView中Image的变换矩阵
            Matrix m = getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];

            //计算Image在屏幕上实际绘制的宽高
            rect.right = (int) (dw * sx);
            rect.bottom = (int) (dh * sy);
            rect.left = rect.right - getWidth();
            rect.top = rect.bottom - getHeight();
        }
        return rect;
    }
}
