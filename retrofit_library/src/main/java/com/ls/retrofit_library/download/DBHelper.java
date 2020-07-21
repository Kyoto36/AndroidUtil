package com.ls.retrofit_library.download;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "download.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DownloadInfo.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static class DownloadInfo{
        public static final String TABLE_NAME = "DownloadInfo";
        public static final String ID = "_id";
        public static final String URL = "_url";
        public static final String SAVE_PATH = "save_path";
        public static final String TOTAL_SIZE = "total_size";
        public static final String ALREADY_SIZE = "already_size";
        public static final String UPDATE_TIME = "update_time";
        public static final String DOWN_STATE = "down_state";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " integer primary key autoincrement," +
                URL + " text," +
                SAVE_PATH + " text," +
                TOTAL_SIZE + " integer," +
                ALREADY_SIZE + " integer," +
                UPDATE_TIME + " integer," +
                DOWN_STATE + " integer)";

        public static DownloadInfo convert(Cursor cursor){
            DownloadInfo info = new DownloadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            info.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
            info.setSavePath(cursor.getString(cursor.getColumnIndex(SAVE_PATH)));
            info.setTotalSize(cursor.getLong(cursor.getColumnIndex(TOTAL_SIZE)));
            info.setAlreadySize(cursor.getLong(cursor.getColumnIndex(ALREADY_SIZE)));
            info.setUpdateTime(cursor.getLong(cursor.getColumnIndex(UPDATE_TIME)));
            info.setDownState(cursor.getInt(cursor.getColumnIndex(DOWN_STATE)));
            return info;
        }

        private long id = -1;
        private String url;
        private String savePath;
        private long totalSize;
        private long alreadySize;
        private long updateTime;
        private int downState;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSavePath() {
            return savePath;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public long getAlreadySize() {
            return alreadySize;
        }

        public void setAlreadySize(long alreadySize) {
            this.alreadySize = alreadySize;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getDownState() {
            return downState;
        }

        public void setDownState(int downState) {
            this.downState = downState;
        }

        @Override
        public String toString() {
            return "DownloadInfo{" +
                    "id=" + id +
                    ", url='" + url + '\'' +
                    ", savePath='" + savePath + '\'' +
                    ", totalSize=" + totalSize +
                    ", alreadySize=" + alreadySize +
                    ", updateTime=" + updateTime +
                    ", downState=" + downState +
                    '}';
        }
    }
}
