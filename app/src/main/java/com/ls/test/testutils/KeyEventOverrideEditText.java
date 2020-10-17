package com.ls.test.testutils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import androidx.appcompat.widget.AppCompatEditText;

import com.ls.comm_util_library.ISingleResultListener;
import com.ls.comm_util_library.LogUtils;

/**
 * @ClassName: CustomEditText
 * @Description:
 * @Author: ls
 * @Date: 2020/10/14 13:36
 */
public class KeyEventOverrideEditText extends AppCompatEditText {
    private ISingleResultListener<KeyEvent,Boolean> mCustomOnKeyListener;

    public KeyEventOverrideEditText(Context context) {
        super(context);
    }

    public KeyEventOverrideEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyEventOverrideEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new OnKeyInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    private class OnKeyInputConnection extends InputConnectionWrapper {

        /**
         * Initializes a wrapper.
         *
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code null}.
         */
        public OnKeyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            LogUtils.INSTANCE.d("OnKeyInputConnection","sendKeyEvent event keycode " + event.getKeyCode() + " action " + event.getAction());
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if(mCustomOnKeyListener != null && mCustomOnKeyListener.onResult(event)){
                    return false;
                }
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            LogUtils.INSTANCE.d("OnKeyInputConnection","deleteSurroundingText beforeLength " + beforeLength + " afterLength " + afterLength);
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (beforeLength == 1 && afterLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public void setCustomOnKeyListener(ISingleResultListener<KeyEvent,Boolean> listener){
        mCustomOnKeyListener = listener;
    }
}
