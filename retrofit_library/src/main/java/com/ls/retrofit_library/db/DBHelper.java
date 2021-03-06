package com.ls.retrofit_library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    public static DBHelper get(Context context){
        if(instance == null){
            synchronized (DBHelper.class){
                if(instance == null){
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    public DBHelper(@Nullable Context context) {
        super(context, "ls_retrofit_util.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DownloadInfo.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
