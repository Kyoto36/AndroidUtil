package com.ls.comm_util_library;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class KeyBoardListener {
    public static void resizeActivity(Activity activity) {
        new KeyBoardListener(activity, null, true);
    }

    public static void resizeView(View view){
        new KeyBoardListener(view,null,true);
    }

    public static void listener(Activity activity, IDoubleListener<Integer, Integer> listener) {
        new KeyBoardListener(activity, listener, false);
    }

    public static void listener(Activity activity, boolean resize, IDoubleListener<Integer, Integer> listener) {
        new KeyBoardListener(activity, listener, resize);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;
    private int contentHeight;
    private boolean isfirst = true;
    private int statusBarHeight;

    private KeyBoardListener(View view, IDoubleListener<Integer, Integer> listener, boolean resize){
        //获取状态栏的高度
        int resourceId = view.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = view.getContext().getResources().getDimensionPixelSize(resourceId);
        mChildOfContent = view;
        //界面出现变动都会调用这个监听事件
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (isfirst) {
                contentHeight = mChildOfContent.getHeight();//兼容华为等机型
                isfirst = false;
            }
            int usableHeightNow = computeUsableHeight();

            if (resize) {
                possiblyResizeChildOfContent(usableHeightNow);
            }
            if (null != listener) {
                listener.onValue(usableHeightNow, contentHeight - usableHeightNow - statusBarHeight);
            }
            usableHeightPrevious = usableHeightNow;
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    private KeyBoardListener(Activity activity, IDoubleListener<Integer, Integer> listener, boolean resize) {
        this(((ViewGroup)(activity.findViewById(android.R.id.content))).getChildAt(0),listener,resize);
    }

    //重新调整跟布局的高度 
    private void possiblyResizeChildOfContent(int usableHeightNow) {
        //当前可见高度和上一次可见高度不一致 布局变动
        if (usableHeightNow != usableHeightPrevious) {
            //int usableHeightSansKeyboard2 = mChildOfContent.getHeight();//兼容华为等机型
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight;
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
            } else {
                frameLayoutParams.height = contentHeight;
            }
            mChildOfContent.requestLayout();
        }
    }

    /**
     * 计算mChildOfContent可见高度     ** @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }
}