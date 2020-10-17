package com.ls.comm_util_library;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * @ClassName: BitmapUtils
 * @Description:
 * @Author: ls
 * @Date: 2020/9/18 17:46
 */
public class BitmapUtils {

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        bgimage.recycle();
        return bitmap;

    }
}
