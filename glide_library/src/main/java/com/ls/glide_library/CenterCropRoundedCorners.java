package com.ls.glide_library;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * A {@link BitmapTransformation} which rounds the corners of a bitmap.
 */
public final class CenterCropRoundedCorners extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedCorners";
  private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

  private final int roundingRadius;

  /**
   * @param roundingRadius the corner radius (in device-specific pixels).
   * @throws IllegalArgumentException if rounding radius is 0 or less.
   */
  public CenterCropRoundedCorners(int roundingRadius) {
    Preconditions.checkArgument(roundingRadius > 0, "roundingRadius must be greater than 0.");
    this.roundingRadius = roundingRadius;
  }

  @Override
  protected Bitmap transform(
          @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
    toTransform = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
    return TransformationUtils.roundedCorners(pool, toTransform, roundingRadius);
//    return toTransform;
  }

  private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
    if (source == null) return null;

    Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
    if (result == null) {
      result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
    }

    Canvas canvas = new Canvas(result);
    Paint paint = new Paint();
    paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    paint.setAntiAlias(true);
    RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
    canvas.drawRoundRect(rectF, roundingRadius, roundingRadius, paint);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof CenterCropRoundedCorners) {
      CenterCropRoundedCorners other = (CenterCropRoundedCorners) o;
      return roundingRadius == other.roundingRadius;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Util.hashCode(ID.hashCode(),
        Util.hashCode(roundingRadius));
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    messageDigest.update(ID_BYTES);

    byte[] radiusData = ByteBuffer.allocate(4).putInt(roundingRadius).array();
    messageDigest.update(radiusData);
  }
}