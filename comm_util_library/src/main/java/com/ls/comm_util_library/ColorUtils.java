package com.ls.comm_util_library;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class ColorUtils {
    public static String getHexString(int color) {
        String s = "#";
        int colorStr = (color & 0xff000000) | (color & 0x00ff0000) | (color & 0x0000ff00) | (color & 0x000000ff);
        s = s + Integer.toHexString(colorStr);
        return s;
    }

    public static String getHtmlColorString(int color){
        String s = "#";
        int colorStr = (color & 0xff0000) | (color & 0x00ff00) | (color & 0x0000ff);
        s = s + Integer.toHexString(colorStr);
        return s;
    }

    public static int changeAlpha(int color,float alpha){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb((int)(255 * alpha),red,green,blue);
    }
}
