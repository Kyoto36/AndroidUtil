package com.ls.glide_library;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.ls.comm_util_library.BitmapUtils;
import com.ls.comm_util_library.LogUtils;
import com.ls.comm_util_library.ObjectUtil;
import com.ls.comm_util_library.TxtUtils;

import java.security.MessageDigest;
import java.util.Objects;

/**
 * A Glide {@link BitmapTransformation} to circle crop an image.  Behaves similar to a
 * {@link FitCenter} transform, but the resulting image is masked to a circle.
 *
 * <p> Uses a PorterDuff blend mode, see http://ssp.impulsetrain.com/porterduff.html. </p>
 */
public class GlideCircleCrop extends BitmapTransformation {
    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.

    public enum BorderType{
        INNER,CENTER,OUTER
    }

    private Paint mBorderPaint;
    private float mBorderWidth;
    private int mBorderColor;

    public GlideCircleCrop() {
    }

    public GlideCircleCrop(float borderWidth, int borderColor) {
        if(borderWidth > 0) {
            mBorderWidth = borderWidth;
            mBorderColor = borderColor;
            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }
    }


    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth() - size) / 2;
        int y = (toTransform.getHeight() - size) / 2;
        Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float radius = size / 2f;
        float bitmapRadius = radius;
        if(mBorderWidth > 0){
            bitmapRadius = radius - mBorderWidth;
        }
        canvas.drawCircle(radius, radius, bitmapRadius, paint);
        if (mBorderPaint != null) {
            float borderRadius = radius - (mBorderWidth / 2);
            canvas.drawCircle(radius, radius, borderRadius, mBorderPaint);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlideCircleCrop that = (GlideCircleCrop) o;
        return Float.compare(that.mBorderWidth, mBorderWidth) == 0 &&
                mBorderColor == that.mBorderColor;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hash(mBorderWidth, mBorderColor);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((getClass().getName() + "_" + mBorderWidth + "_" + mBorderColor).getBytes());
    }
}
