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
import android.util.Log;

import androidx.core.content.FileProvider;

import com.ls.comm_util_library.thumbnails.BucketBean;
import com.ls.comm_util_library.thumbnails.ImageBean;

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
     * 根据图片ID获取Uri
     * @param imageId 在系统图片数据库中的ID
     * @return
     */
    public static Uri getImageUri(int imageId){
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imageId);
    }

    /**
     * 获取所有相册分组
     * @param context
     * @param preCount 预先加载几张图片，可以作为分组的图标
     * @return
     */
    public static Map<Integer, BucketBean> loadBuckets(Context context,int preCount){
        ContentResolver cts = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = cts.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,MediaStore.Images.Media.DATE_MODIFIED + " desc");
        return getBucketBeans(cursor,preCount);
    }

    private static Map<Integer,BucketBean> getBucketBeans(Cursor cursor,int preCount){
        if(cursor == null) return null;
        Map<Integer,BucketBean> map = new HashMap<>();
        if(cursor.moveToFirst()){
            int imageId;
            int bucketId;
            String bucketName;
            String imageName;
            String imagePath;
            int imageIdIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int bucketIdIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int imageNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int imagePathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            BucketBean bean;
            ImageBean imageBean;
            do {
                imageId = cursor.getInt(imageIdIndex);
                bucketId = cursor.getInt(bucketIdIndex);
                bucketName = cursor.getString(bucketNameIndex);
                imageName = cursor.getString(imageNameIndex);
                imagePath = cursor.getString(imagePathIndex);
                bean = map.get(bucketId);
                if(bean == null){
                    bean = new BucketBean();
                    bean.setId(bucketId);
                    bean.setName(bucketName);
                    bean.setImageIds(new ArrayList<>());
                    bean.setImages(new ArrayList<>());
                    map.put(bucketId,bean);
                }
                bean.getImageIds().add(imageId);
                if(bean.getImages().size() < preCount){
                    imageBean = new ImageBean();
                    imageBean.setId(imageId);
                    imageBean.setName(imageName);
                    imageBean.setPath(imagePath);
                    imageBean.setUri(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imageId));
                    bean.getImages().add(imageBean);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return map;
    }

    /**
     * 根据获取最近的图片集合
     * @param context
     * @param limit 获取到多久之后 单位秒
     * @return
     */
    public static List<ImageBean> loadNewestImages(Context context, long limit){
        return loadImages(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,limit);
    }

    /**
     * 获取所有图片
     * @param context
     * @return
     */
    public static List<ImageBean> loadImages(Context context){
        return loadImages(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,0);
    }

    /**
     * 根据Uri获取图片集合
     * @param context
     * @return
     */
    public static List<ImageBean> loadImages(Context context, Uri uri){
        return loadImages(context, uri,0);
    }


    /**
     * 根据Uri获取图片集合
     * @param context
     * @param uri
     * @param limit 获取到多久之后 单位秒
     * @return
     */
    public static List<ImageBean> loadImages(Context context, Uri uri, long limit){
        String selection = null;
        String[] selectionArgs = null;
        if(limit > 0){
            selection = MediaStore.Images.Media.DATE_MODIFIED + " > ?";
            selectionArgs = new String[]{"" + limit};
        }
        ContentResolver cts = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media._ID,MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        Cursor cursor = cts.query(uri,projection,selection,selectionArgs,MediaStore.Images.Media.DATE_MODIFIED + " desc");
        return getImageBeans(cursor);
    }

    private static List<ImageBean> getImageBeans(Cursor cursor){
        List<ImageBean> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            int id;
            String name;
            String path;
            Uri uri;
            int idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int dateIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
            ImageBean bean;
            do {
               id = cursor.getInt(idIndex);
               name = cursor.getString(nameIndex);
               path = cursor.getString(pathIndex);
               uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id);
               bean = new ImageBean();
               bean.setId(id);
               bean.setName(name);
               bean.setPath(path);
               bean.setUri(uri);
               list.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
