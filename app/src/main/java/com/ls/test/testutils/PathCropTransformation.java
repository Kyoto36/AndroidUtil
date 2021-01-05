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

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * @ClassName: PathCropTransformation
 * @Description:
 * @Author: ls
 * @Date: 2020/12/23 17:37
 */
public class PathCropTransformation extends BitmapTransformation {
    private static final String ID = "com.ls.glide_library.PathCropTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private Path mPath;
    private float mRotate;
    private float mBorderWidth;
    private int mBorderColor;

    public PathCropTransformation(Path mPath,float rotate,float borderWidth,int borderColor) {
        this.mPath = mPath;
        this.mRotate = rotate;
        this.mBorderWidth = borderWidth;
        this.mBorderColor = borderColor;
    }

    public PathCropTransformation(Path mPath) {
        this(mPath,0,0,-1);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        toTransform = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.save();
        canvas.rotate(mRotate,(float)outWidth / 2,(float)outHeight / 2);
        Paint tempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(mBorderWidth > 0){
            tempPaint.setStyle(Paint.Style.FILL);
            tempPaint.setColor(mBorderColor);
            canvas.drawPath(mPath,tempPaint);
            tempPaint.setColor(Color.WHITE);
        }
        canvas.restore();
        int radius = outWidth < outHeight ? outWidth / 2 : outHeight / 2;
        Path scalePath = new Path(mPath);
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setRotate(mRotate, radius, radius);
        if(mBorderWidth > 0){
            float scaleWidth = outWidth - (mBorderWidth * 2);
            float scaleHeight = outHeight - (mBorderWidth * 2);
            scaleMatrix.postScale(scaleWidth / outWidth, scaleHeight / outHeight, radius, radius);
            scalePath.transform(scaleMatrix);
        }
        tempPaint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        tempPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawPath(scalePath,tempPaint);
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PathCropTransformation) {
            PathCropTransformation other = (PathCropTransformation) obj;
            return mPath == other.mPath && mRotate == other.mRotate && mBorderWidth == other.mBorderWidth && mBorderColor == other.mBorderColor;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(),
                mPath.hashCode() + Util.hashCode(mRotate) + Util.hashCode(mBorderWidth) + Util.hashCode(mBorderColor));
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] hashCodeData = ByteBuffer.allocate(4).putInt(hashCode()).array();
        messageDigest.update(hashCodeData);
    }
}
