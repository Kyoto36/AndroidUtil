package com.ls.custom_view_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 正方形的ImageView
 */
public class SquareImageView extends AppCompatImageView {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int mOrientation = HORIZONTAL;

    public SquareImageView(Context context) {
        this(context,null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SquareImageView, defStyleAttr, 0);
        mOrientation = ta.getInt(R.styleable.SquareImageView_orientation,0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mOrientation == HORIZONTAL) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
        else{
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }
}