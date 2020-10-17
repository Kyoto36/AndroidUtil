package com.ls.comm_util_library;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 * @ClassName: CustomLinkMovementMethod
 * @Description: 修复点击之后背景色不消失的问题，以及在可滑动的父窗体中选中背景色失效的问题
 *               使用该方式的前提是，该view的所有上级父窗体都不能使用android:descendantFocusability="blocksDescendants"
 *               不然的话焦点将永远下不来，点击背景变色的效果也就出不来了,如果必须上面的属性,就是用CustomTextView
 *               CustomTextView 在 custom_util_library中
 *               如果父窗体不能滑动就另当辩论了
 * @Author: ls
 * @Date: 2020/9/29 10:50
 */
public class CustomLinkMovementMethod extends ScrollingMovementMethod {
    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();


            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);


            ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);

            if (links.length != 0) {
                ClickableSpan link = links[0];
                if (action == MotionEvent.ACTION_UP) {
                    link.onClick(widget);
                    // 点击之后消失
                    Selection.removeSelection(buffer);
                    widget.clearFocus();
                } else {
                    // 在可滑动的父窗体中选中背景色失效 需要配合focusableInTouchMode="true"
                    // 在这里调用的话会有点频繁
                    // widget.setFocusableInTouchMode(true);
                    widget.requestFocus();
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link),
                            buffer.getSpanEnd(link));
                    if (action == MotionEvent.ACTION_MOVE) {
                        return super.onTouchEvent(widget, buffer, event);
                    }
                }
                return true;
            } else {
                Selection.removeSelection(buffer);
                widget.clearFocus();
            }
        }
        else if(action == MotionEvent.ACTION_CANCEL){
            // 如果和父窗口的滑动方向相同就会触发取消，这时候取消掉选中
            Selection.removeSelection(buffer);
        }
        return super.onTouchEvent(widget, buffer, event);

    }

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new CustomLinkMovementMethod();

        return sInstance;
    }

    private static CustomLinkMovementMethod sInstance;
}
