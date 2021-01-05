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
public class StrategyTransformation extends BitmapTransformation {
    private static final String ID = "com.ls.glide_library.StrategyTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private ITransformationStrategy mStrategy;

    public StrategyTransformation(ITransformationStrategy strategy) {
        this.mStrategy = strategy;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return mStrategy.transform(pool, toTransform, outWidth, outHeight);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof StrategyTransformation) {
            StrategyTransformation other = (StrategyTransformation) obj;
            return mStrategy == other.mStrategy || mStrategy.equals(other.mStrategy);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), mStrategy.hashCode());
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] hashCodeData = ByteBuffer.allocate(4).putInt(hashCode()).array();
        messageDigest.update(hashCodeData);
    }

    /**
     * @ClassName: com.ls.test.testutils.StrategyTransformation.ITransformationStrategy
     * @Description:
     * @Author: ls
     * @Date: 2020/12/24 14:52
     */
    public static interface ITransformationStrategy {
        Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight);
    }
}
