package com.ls.custom_view_library.puzzle_verify;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.ls.comm_util_library.IStateListener;
import com.ls.comm_util_library.Util;
import com.ls.custom_view_library.R;

public class CaptchaView extends LinearLayout {

    public enum Mode{
        /**
         * 带滑动条验证模式
         */
        MODE_BAR,
        /**
         * 不带滑动条验证，手触模式
         */
        MODE_NONBAR,
    }

    private Mode mMode = Mode.MODE_BAR;

    private PuzzleVerifyView mVerifyView;
    private TextSeekBar mSeekbar;

    private IStateListener<Void> mStateListener;

    public CaptchaView(Context context) {
        this(context,null);
    }

    public CaptchaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CaptchaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        mVerifyView = new PuzzleVerifyView(context, attrs);
        mVerifyView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LayoutParams verifyLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        verifyLp.weight = 4;
        mSeekbar = new TextSeekBar(context, attrs);
        setSeekBarStyle(R.drawable.po_seekbar,R.drawable.thumb);
        LayoutParams seekbarLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        seekbarLp.topMargin = (int)Util.Companion.dp2px(8F);
        addView(mVerifyView,verifyLp);
        addView(mSeekbar,seekbarLp);

        initListener();
    }

    private void initListener() {
        mVerifyView.setAccessListener(new IStateListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mSeekbar.setEnabled(false);
                if(null != mStateListener){
                    mStateListener.onSuccess(aVoid);
                }
            }

            @Override
            public void onFailed(Exception e) {
                mSeekbar.setEnabled(false);
                if(null != mStateListener){
                    mStateListener.onFailed(e);
                }
            }
        });
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVerifyView.move(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mVerifyView.start();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVerifyView.stop();
            }
        });
    }

    public void setStateListener(IStateListener<Void> listener){
        mStateListener = listener;
    }

    public void setMode(Mode mode){
        mMode = mode;
        mSeekbar.setVisibility(mode == Mode.MODE_BAR ? View.VISIBLE : View.GONE);
        mSeekbar.setEnabled(mode == Mode.MODE_BAR);
        mVerifyView.setMode(mode);
    }

    public ImageView getPuzzleView(){
        return mVerifyView;
    }

    public void reload(){
        mSeekbar.setProgress(0);
        mVerifyView.reload();
        mSeekbar.setEnabled(true);
    }

    public void setSeekBarStyle(@DrawableRes int progressDrawable, @DrawableRes int thumbDrawable){
        mSeekbar.setProgressDrawable(getResources().getDrawable(progressDrawable));
        mSeekbar.setThumb(getResources().getDrawable(thumbDrawable));
        mSeekbar.setThumbOffset(0);
    }
}
