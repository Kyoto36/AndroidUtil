package com.ls.comm_util_library;

import androidx.annotation.ColorInt;

public class PreBackgroundColorSpan{

    @ColorInt
    private int color;

    private int start;

    private int end;

    public PreBackgroundColorSpan(@ColorInt int color,int start,int end){
        this.color = color;
        this.start = start;
        this.end = end;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
