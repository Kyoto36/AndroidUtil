package com.ls.test.testutils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;
import com.ls.comm_util_library.ColorUtils;
import com.ls.comm_util_library.PathFactory;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * @ClassName: PathCropTransformation
 * @Description:
 * @Author: ls
 * @Date: 2020/12/23 17:37
 */
public class CustomCropTransformation extends BitmapTransformation {
    private static final String ID = "com.qhooplay.yushu.util.CustomCropTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private Path mPath;

    public CustomCropTransformation(float radius) {
        this.mPath = PathFactory.getSquare(6,radius,com.ls.comm_util_library.Util.Companion.dp2px(1F));
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        toTransform = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint tempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tempPaint.setStyle(Paint.Style.STROKE);
        tempPaint.setStrokeWidth(com.ls.comm_util_library.Util.Companion.dp2px(1F));
        tempPaint.setColor(ColorUtils.changeAlpha(Color.parseColor("#704F3C"),0.15F));
        canvas.drawPath(mPath,tempPaint);
        tempPaint.setColor(Color.parseColor("#704F3C"));
        tempPaint.setStyle(Paint.Style.FILL);
        canvas.save();
        canvas.rotate(30,(float)outWidth / 2,(float)outHeight / 2);
        canvas.drawPath(mPath,tempPaint);
        canvas.restore();
        int radius = outWidth < outHeight ? outWidth / 2 : outHeight / 2;
        Path scalePath = new Path(mPath);
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setRotate(30, radius, radius);
        float scaleWidth = outWidth - (com.ls.comm_util_library.Util.Companion.dp2px(1F) * 2);
        float scaleHeight = outHeight - (com.ls.comm_util_library.Util.Companion.dp2px(1F) * 2);
        scaleMatrix.postScale(scaleWidth / outWidth, scaleHeight / outHeight, radius, radius);
        scalePath.transform(scaleMatrix);
        tempPaint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        tempPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawPath(scalePath,tempPaint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        byte[] hashCodeData = ByteBuffer.allocate(4).putInt(mPath.hashCode()).array();
        messageDigest.update(hashCodeData);
    }
}
