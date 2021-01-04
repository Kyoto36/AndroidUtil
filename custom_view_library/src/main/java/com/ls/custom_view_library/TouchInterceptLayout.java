package com.ls.custom_view_library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ClassName: TouchInterceptView
 * @Description:
 * @Author: ls
 * @Date: 2020/10/23 14:04
 */
public class TouchInterceptLayout extends FrameLayout {
    public TouchInterceptLayout(@NonNull Context context) {
        super(context);
    }

    public TouchInterceptLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchInterceptLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
//        return true;
    }
}
