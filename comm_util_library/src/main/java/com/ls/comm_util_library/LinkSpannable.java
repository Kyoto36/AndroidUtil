package com.ls.comm_util_library;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * @ClassName: LinkSpannable
 * @Description:
 * @Author: ls
 * @Date: 2020/9/23 11:37
 */
public class LinkSpannable extends SpannableString {
    protected int mColor;
    protected CharSequence mClickResult;
    protected ISingleListener<CharSequence> mClickListener;
    protected boolean mUnderline = true;
    protected CharSequence mSource;

    public LinkSpannable(int color, CharSequence source, CharSequence clickResult, boolean underline) {
        super(source);
        this.mSource = source;
        this.mColor = color;
        this.mClickResult = clickResult;
        this.mUnderline = underline;
        setSpan(new ForegroundColorSpan(color), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void setClickListener(ISingleListener<CharSequence> clickListener) {
        this.mClickListener = clickListener;
        setSpan(new CustomClickSpan(toString(), mColor, mUnderline, (ISingleListener<CharSequence>) charSequence -> clickListener.onValue(mClickResult)),0,toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public CharSequence getClickResult() {
        return mClickResult;
    }

    public void setClickResult(CharSequence mClickResult) {
        this.mClickResult = mClickResult;
    }

    public CharSequence getSource() {
        return mSource;
    }

    public void setSource(CharSequence mSource) {
        this.mSource = mSource;
    }
}
