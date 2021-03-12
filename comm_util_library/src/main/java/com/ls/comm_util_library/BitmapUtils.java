package com.ls.comm_util_library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public static Size getSize(Uri uri, Context context){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        InputStream in = AndroidFileUtils.getInputStreamByUri(context, uri);
        BitmapFactory.decodeStream(in,null,options);
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Size(options.outWidth,options.outHeight);
    }

    public static Size getSize(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        return new Size(options.outWidth,options.outHeight);
    }

    /**
     * 计算采样率
     * @param srcWidth
     * @param srcHeight
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calcSampleSize(int srcWidth,int srcHeight,int reqWidth,int reqHeight){
        int sampleSize = 1;
        while (srcWidth / sampleSize > reqWidth || srcHeight / sampleSize > reqHeight){
            sampleSize *= 2;
        }
        return sampleSize;
    }

    /**
     * 计算采样率（以宽为准）
     * @param srcWidth
     * @param reqWidth
     * @return
     */
    public static int calcWidthSampleSize(int srcWidth,int reqWidth){
        int sampleSize = 1;
        while (srcWidth / sampleSize > reqWidth){
            sampleSize *= 2;
        }
        return sampleSize;
    }

    /**
     * 保存图片
     * @param bitmap
     * @param path
     */
    public static void saveBitmap(Bitmap bitmap,String path){
        if(bitmap == null){
            return;
        }
        try {
            OutputStream os = FileUtils.getOutputStreamByFile(path);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片
     * @param bitmap
     * @param path
     */
    public static void saveBitmap(Bitmap bitmap, String path, Bitmap.CompressFormat format){
        if(bitmap == null){
            return;
        }
        try {
            OutputStream os = FileUtils.getOutputStreamByFile(path);
            bitmap.compress(Bitmap.CompressFormat.PNG,75,os);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取assets下的图片
     * @param context
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromAssets(Context context,String filePath){
        try {
            InputStream in = AndroidFileUtils.getInputStreamByAsset(context, filePath);
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成圆角图片
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static boolean isGif(File file){
        try {
            return isGif(FileUtils.getInputStreamByFile(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isGif(InputStream inputStream){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        return options.outMimeType.toLowerCase().endsWith("gif");
    }

}
