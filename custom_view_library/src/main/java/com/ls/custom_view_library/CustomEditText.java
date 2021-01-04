package com.ls.custom_view_library;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.ls.comm_util_library.ColorUtils;
import com.ls.comm_util_library.LogUtils;

/**
 * @ClassName: CustomEditText
 * @Description:
 * @Author: ls
 * @Date: 2020/10/16 16:33
 */
public class CustomEditText extends AppCompatEditText {
    private BackgroundColorSpan mBackgroundColorSpan;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int off;
        Spannable text = getText();
        if(text == null){
            return super.onTouchEvent(event);
        }
        if (action == MotionEvent.ACTION_DOWN) {
            off = calcOff(event);
            if (off != -1) {
                CustomTextView.TextClickSpan[] spans = text.getSpans(off, off, CustomTextView.TextClickSpan.class);
                if (spans != null && spans.length > 0 && spans[0] != null) {
                    removeSpan(mBackgroundColorSpan);
                    CustomTextView.TextClickSpan click = spans[0];
                    if (click.getColor() != -1) {
                        int bgColor = ColorUtils.changeAlpha(spans[0].getColor(),0.5F);
                        mBackgroundColorSpan = new BackgroundColorSpan(bgColor);
                        setSpan(mBackgroundColorSpan, text.getSpanStart(click), text.getSpanEnd(click));
                        return true;
                    }
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            removeSpan(mBackgroundColorSpan);
            off = calcOff(event);
            if (off != -1) {
                CustomTextView.TextClickSpan[] spans = text.getSpans(off, off, CustomTextView.TextClickSpan.class);
                if (spans != null && spans.length > 0 && spans[0] != null) {
                    spans[0].onClick();
                    return true;
                }
            }

        } else if (action == MotionEvent.ACTION_CANCEL) {
            removeSpan(mBackgroundColorSpan);
        }
        return super.onTouchEvent(event);
    }

    private int calcOff(MotionEvent event) {
        int off = -1;
        if(getText() != null) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();
            x += getScrollX();
            y += getScrollY();
            int line = getLayout().getLineForVertical(y);
            float lineWidth = getLayout().getLineWidth(line);
            if(lineWidth > x){
                off = getLayout().getOffsetForHorizontal(line, x);
            }
        }
        return off;
    }

    private void setSpan(Object obj, int start, int end) {
        if (getText() != null && obj != null) {
            getText().setSpan(obj, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void removeSpan(Object obj) {
        if (getText() != null && obj != null) {
            getText().removeSpan(obj);
        }
    }
}
