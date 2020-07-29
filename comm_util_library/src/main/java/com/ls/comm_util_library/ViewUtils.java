package com.ls.comm_util_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.FloatRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewUtils {

    /**
     * 拦截TextView中的链接点击事件，用于自定义跳转
     * @param view TextView
     * @param listener 点击监听
     */
    public static void interceptUrlClick(TextView view, ISingleListener<String> listener){
        view.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = view.getText();
        if(text instanceof Spannable){
            Spannable sp = (Spannable) text;
            URLSpan[] urls = sp.getSpans(0,text.length(),URLSpan.class);
            if(urls.length == 0){
                return;
            }
            SpannableStringBuilder spannable = new SpannableStringBuilder(text);
            for (URLSpan url: urls){
                spannable.setSpan(new CustomClickSpan(url.getURL(), listener),sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            view.setText(spannable);
        }
    }

    public static void setSelectedView(View view, boolean isSelected) {
        if (view == null || view.isSelected() == isSelected) {
            return;
        }
        view.setSelected(isSelected);
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setSelectedView(((ViewGroup) view).getChildAt(i), isSelected);
            }
        }
    }

    public static void setSelectedView(ViewGroup viewGroup, int position) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (position == i) {
                setSelectedView(viewGroup.getChildAt(i),true);
            } else if (viewGroup.getChildAt(i).isSelected()) {
                setSelectedView(viewGroup.getChildAt(i),false);
            }
        }
    }

    public static void setSelectedView(ViewGroup viewGroup, View child) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (child == viewGroup.getChildAt(i)) {
                setSelectedView(viewGroup.getChildAt(i),true);
            } else if (viewGroup.getChildAt(i).isSelected()) {
                setSelectedView(viewGroup.getChildAt(i),false);
            }
        }
    }

    public static void setViewVisible(ViewGroup viewGroup, View... childs){
        if(childs == null || childs.length <= 0) return;
        List<View> views = Arrays.asList(childs);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (views.contains(viewGroup.getChildAt(i))) {
                viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
            }
            else{
                viewGroup.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    public static void setChildVisibility(ViewGroup viewGroup,int Visibility){
        for (int i = 0; i < viewGroup.getChildCount(); i++){
            viewGroup.getChildAt(i).setVisibility(Visibility);
        }
    }

    public static void showKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static int generateBitmapYAverage(Bitmap bitmap) {
        ArrayList<Integer> pixels = getPicturePixel(bitmap);
        long totalY = 0;
        for (int pixel : pixels) {
            totalY += (Color.red(pixel) * 0.299f + Color.green(pixel) * 0.587f + Color.blue(pixel) * 0.114f);
        }
        return (int) (totalY / pixels.size());
    }

    /**
     * 获得图片像素数组的方法
     *
     * @param bitmap 传入的bitmap值
     */
    public static ArrayList<Integer> getPicturePixel(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // 保存所有的像素的数组，图片宽×高
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        ArrayList<Integer> rgb = new ArrayList<>();
        for (int i = 0; i < pixels.length; i++) {
            int clr = pixels[i];
            int red = (clr & 0x00ff0000) >> 16; // 取高两位
            int green = (clr & 0x0000ff00) >> 8; // 取中两位
            int blue = clr & 0x000000ff; // 取低两位
            int color = Color.rgb(red, green, blue);
            //除去白色和黑色
            if (color != Color.WHITE && color != Color.BLACK) {
                rgb.add(color);
            }
        }
        return rgb;
    }

    private static final float[] BT_SELECTED_DARK = new float[] { 1, 0, 0, 0, -50, 0, 1, 0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0 };

    public static void addImageClickDrak(final ImageView imageView, View clickView) {
        addImageClickDrak(imageView, clickView,null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void addImageClickDrak(final ImageView imageView, View clickView, View.OnClickListener clickListener) {
        if(clickView == null){
            clickView = imageView;
        }
        clickView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    imageView.setColorFilter(new ColorMatrixColorFilter(BT_SELECTED_DARK));
                    return false;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    imageView.clearColorFilter();
                    break;
            }
            return false;
        });
        clickView.setOnClickListener(clickListener);
    }

    public static void addViewClickAlpha(final View view, @FloatRange(from = 0,to = 1) float alpha){
        addViewClickAlpha(view, alpha,null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void addViewClickAlpha(final View view, @FloatRange(from = 0,to = 1) float alpha, View.OnClickListener clickListener){
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.setAlpha(alpha);
                    return false;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    view.setAlpha(1);
                    break;
            }
            return false;
        });
        view.setOnClickListener(clickListener);
    }

}
