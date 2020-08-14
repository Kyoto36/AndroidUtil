package com.ls.comm_util_library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import com.ls.comm_util_library.thumbnails.BucketBean;
import com.ls.comm_util_library.thumbnails.ThumbnailBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaUtils {
    private final static String TAG = MediaUtils.class.getSimpleName();
    public final static int OPEN_ALBUM_VIDEO_REQUEST_CODE = 1001;
    public final static int OPEN_CAMERA_VIDEO_REQUEST_CODE = 1002;
    public final static int OPEN_ALBUM_PHOTO_REQUEST_CODE = 1003;
    public final static int OPEN_CAMERA_PHOTO_REQUEST_CODE = 1004;
    public static void openSystemVideoAlbum(Activity activity){
        activity.startActivityForResult(getSystemAlbumIntent(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"video/*"), OPEN_ALBUM_VIDEO_REQUEST_CODE);
    }

    public static void openSystemVideoCamera(Activity activity, String savePath){
        activity.startActivityForResult(getSystemCameraIntent(activity, MediaStore.ACTION_VIDEO_CAPTURE,savePath), OPEN_CAMERA_VIDEO_REQUEST_CODE);
    }

    public static void openSystemPhotoAlbum(Activity activity){
        activity.startActivityForResult(getSystemAlbumIntent(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*"), OPEN_ALBUM_PHOTO_REQUEST_CODE);
    }

    public static void openSystemPhotoCamera(Activity activity, String savePath){
        activity.startActivityForResult(getSystemCameraIntent(activity, MediaStore.ACTION_IMAGE_CAPTURE,savePath), OPEN_CAMERA_PHOTO_REQUEST_CODE);
    }

    private static Intent getSystemAlbumIntent(Uri uri, String type){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(uri,type);
        return intent;
    }

    private static Intent getSystemCameraIntent(Context context, String action, String savePath){
        File file = new File(savePath);
        Intent intent = new Intent(action);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.qhooplay.YuShu.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        if(uri == null) return "";
        if (Build.VERSION.SDK_INT >= 19) {
            return getRealPathFromUriAboveApi19(context, uri);
        } else {
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) {
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = "";
        LogUtils.INSTANCE.d(TAG,uri.toString());
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 获取所有相册分组
     * @param context
     * @return
     */
    public static Map<Integer, BucketBean> loadBuckets(Context context){
        ContentResolver cts = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media._ID};
        Cursor cursor = cts.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);
        return getBucketBeans(cursor,context);
    }

    private static Map<Integer,BucketBean> getBucketBeans(Cursor cursor,Context context){
        if(cursor == null) return null;
        Map<Integer,BucketBean> map = new HashMap<>();
        if(cursor.moveToFirst()){
            int imageId;
            int bucketId;
            String bucketName;
            int imageIdIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int bucketIdIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            BucketBean bean;
            do {
                imageId = cursor.getInt(imageIdIndex);
                bucketId = cursor.getInt(bucketIdIndex);
                bucketName = cursor.getString(bucketNameIndex);
                bean = map.get(bucketId);
                if(bean == null){
                    bean = new BucketBean();
                    bean.setId(bucketId);
                    bean.setName(bucketName);
                    bean.setImageIds(new ArrayList<>());
                    bean.setThumbnails(new ArrayList<>());
                    map.put(bucketId,bean);
                }
                bean.getImageIds().add(imageId);
                bean.getThumbnails().add(loadThumbnail(imageId,context));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return map;
    }

    /**
     * 获取单个图片缩略图
     * @param imageId 图片Id
     * @param context
     * @return
     */
    public static ThumbnailBean loadThumbnail(int imageId, Context context){
        ContentResolver cts = context.getContentResolver();
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = cts.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,projection,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?", new String[] {"" + imageId},null);
        return getThumbnail(cursor);
    }

    private static ThumbnailBean getThumbnail(Cursor cursor){
        if(cursor == null) return null;
        if(cursor.moveToFirst()){
            int idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            ThumbnailBean bean = new ThumbnailBean();
            bean.setId(cursor.getInt(idColumn));
            bean.setImageId(cursor.getInt(imageIdColumn));
            bean.setData(cursor.getString(dataColumn));
            return bean;
        }
        return null;
    }

    /**
     * 获取所有图片缩略图
     * @param context
     * @return
     */
    public static List<ThumbnailBean> loadThumbnails(Context context){
        ContentResolver cts = context.getContentResolver();
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = cts.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,projection,null,null,null);
        return getThumbnailsBeans(cursor);

    }

    private static List<ThumbnailBean> getThumbnailsBeans(Cursor cursor){
        List<ThumbnailBean> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            int id;
            int imageId;
            String data;
            int idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            ThumbnailBean bean;
            do {
                // Get the field values
                id = cursor.getInt(idColumn);
                imageId = cursor.getInt(imageIdColumn);
                data = cursor.getString(dataColumn);

                // Do something with the values.
                bean = new ThumbnailBean();
                bean.setId(id);
                bean.setImageId(imageId);
                bean.setData(data);
                list.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
