package com.ls.comm_util_library;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * android文件帮助类（兼容android10）
 */
public abstract class AndroidFileUtils {

    /**
     * 读android项目中assets下的文件，并转成String，当然要保证文件中存的是字符串
     * @param context android下的context
     * @param fileName 需要读取的文件名称
     * @param isOnceRead 是否一次性读完流的内容（如果内容比较多设置为false）
     * @return 转换好的字符串
     */
    public static String readAssetText(Context context,String fileName,boolean isOnceRead){
        try {
            return FileUtils.inputStream2String(getInputStreamByAsset(context, fileName),true,isOnceRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读android项目中assets下的文件，并转成String，当然要保证文件中存的是字符串
     * @param context android下的context
     * @param fileName 需要读取的文件名称
     * @return 转换好的字符串
     */
    public static String readAssetText(Context context,String fileName){
        return readAssetText(context, fileName,false);
    }

    /**
     * 读取asset下的输入流（装饰了BufferedInputStream）
     * @param context android下的context
     * @param fileName 需要读取的文件名称
     * @return 输入流
     * @throws IOException 获取输入流会产生异常，调用方自行处理
     */
    public static InputStream getInputStreamByAsset(Context context, String fileName) throws IOException {
        return new BufferedInputStream(context.getAssets().open(fileName));
    }

    /**
     * 计算文件大小（包括目录）
     * @param context android中的上下文
     * @param path 目标文件路径
     * @return 返回格式化之后的大小
     */
    public static String calcFileSize(Context context, String path) {
        return calcFileSize(context, new File(path));
    }

    /**
     * 计算文件大小（包括目录）
     * @param context android中的上下文
     * @param file 目标文件
     * @return 返回格式化之后的大小
     */
    public static String calcFileSize(Context context, File file) {
        long size = FileUtils.calcFileSize(file);
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 根据uri获取文件大小，android Q之后外部文件不能用路径获取了
     * @param context android中的上下文
     * @param uri 目标uri
     * @return 文件大小
     */
    public static long getFileSizeByUri(Context context, Uri uri){
        long size = 0;
        String[] projection = {MediaStore.MediaColumns.SIZE};
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null){
            try{
                if(cursor.moveToFirst()){
                    size = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));
                }
            }
            finally {
                cursor.close();
            }
        }
        return size;
    }
}
