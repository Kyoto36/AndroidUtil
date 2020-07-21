package com.ls.custom_view_library;

import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.ls.comm_util_library.LogUtils;

public class CardTransformer implements ViewPager.PageTransformer {
    private static float MIN_SCALE_X = 0.9f;//最小缩放值
    private static float MIN_SCALE_Y = 0.8f;//最小缩放值

    @Override
    public void transformPage(View page, float position) {

        float scaleX;//view  应缩放的值
        float scaleY;
        if (position > 1 || position < -1) {
            scaleX = MIN_SCALE_X;
            scaleY = MIN_SCALE_Y;
        } else if (position < 0) {
            scaleX = MIN_SCALE_X + (1 + position) * (1 - MIN_SCALE_X);
            scaleY = MIN_SCALE_Y + (1 + position) * (1 - MIN_SCALE_Y);
        } else {
            scaleX = MIN_SCALE_X + (1 - position) * (1 - MIN_SCALE_X);
            scaleY = MIN_SCALE_Y + (1 - position) * (1 - MIN_SCALE_Y);
        }
        page.setScaleY(scaleY);
        page.setScaleX(scaleX);
    }
}