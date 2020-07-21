package com.ls.video_player_library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


/**
 * Author: yanzhikai
 * Description: 中间用于显示状态的Layout
 * Email: yanzhikai_yjk@qq.com
 */
public class ChangeTipView extends RelativeLayout {
    private static final String TAG = "gesturetest";
    private ImageView iv_center;
    private ProgressBar pb;
    private HideRunnable mHideRunnable;
    private int duration = 1000;

    public ChangeTipView(Context context) {
        super(context);
        init(context);
    }

    public ChangeTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.ls_library_change_tip,this);
        iv_center = findViewById(R.id.iv_center);
        pb = findViewById(R.id.pb);

        mHideRunnable = new HideRunnable();
        ChangeTipView.this.setVisibility(GONE);
    }

    //显示
    public void show(){
        setVisibility(VISIBLE);
        removeCallbacks(mHideRunnable);
        postDelayed(mHideRunnable,duration);
    }

    //设置进度
    public void setProgress(int progress){
        pb.setProgress(progress);
        Log.d(TAG, "setProgress: " +progress);
    }

    //设置持续时间
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMax(int max){
        pb.setMax(max);
    }

    //设置显示图片
    public void setImageResource(int resource){
        iv_center.setImageResource(resource);
    }

    //隐藏自己的Runnable
    private class HideRunnable implements Runnable {
        @Override
        public void run() {
            ChangeTipView.this.setVisibility(GONE);
        }
    }

    public void hide(){
        removeCallbacks(mHideRunnable);
        mHideRunnable.run();
    }
}
