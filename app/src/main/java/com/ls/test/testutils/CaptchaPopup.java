package com.ls.test.testutils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ls.custom_view_library.puzzle_verify.CaptchaView;

public class CaptchaPopup {

    private PopupWindow mPopupWindow;
    private CaptchaView mCaptchaView;

    public CaptchaPopup(Context context){
        mPopupWindow = new PopupWindow(LayoutInflater.from(context).inflate(R.layout.popup_puzzle,null));
        mCaptchaView = mPopupWindow.getContentView().findViewById(R.id.captcha);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
    }

    public void show(int drawableId,ViewGroup parent){
        if(mPopupWindow.isShowing()) return;
        mCaptchaView.getPuzzleView().setImageResource(drawableId);
        mCaptchaView.reload();
        mPopupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
    }

    public void dismiss(){
        if(mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }
}
