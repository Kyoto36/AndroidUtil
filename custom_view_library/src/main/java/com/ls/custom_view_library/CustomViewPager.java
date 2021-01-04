package com.ls.custom_view_library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
	private boolean noScroll = true;
	int lastX = -1;
	int lastY = -1;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomViewPager(Context context) {
		super(context);
	}

	public void setNoScroll(boolean noScroll) {
		this.noScroll = noScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		/* return false;//super.onTouchEvent(arg0); */
		if (noScroll)
			return false;
		else
			return super.onTouchEvent(arg0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (noScroll)
			return false;
		else{
			int x = (int) arg0.getRawX();
			int y = (int) arg0.getRawY();
			int dealtX = 0;
			int dealtY = 0;

			switch (arg0.getAction()) {
				case MotionEvent.ACTION_DOWN:
					dealtX = 0;
					dealtY = 0;
					// 保证子View能够接收到Action_move事件
					getParent().requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_MOVE:
					dealtX += Math.abs(x - lastX);
					dealtY += Math.abs(y - lastY);
					// 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
					if (dealtX >= dealtY) {
						getParent().requestDisallowInterceptTouchEvent(true);
					} else {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
					Log.i("CustomViewPager", "dealtX:=" + dealtX + " dealtY:=" + dealtY);
					lastX = x;
					lastY = y;
					break;
				default:
					break;

			}
			return super.onInterceptTouchEvent(arg0);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}

}