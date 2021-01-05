package com.ls.test.testutils.db_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ClassName: ChatDBHelper
 * @Description:
 * @Author: ls
 * @Date: 2020/11/5 10:24
 */
public class ChatDBHelper extends SQLiteOpenHelper {

    public static Map<String,ChatDBHelper> instanceMap = new ConcurrentHashMap<>();

    public static ChatDBHelper get(String uid,Context context){
        ChatDBHelper instance = instanceMap.get(uid);
        if(instance == null){
            synchronized (ChatDBHelper.class){
                instance = instanceMap.get(uid);
                if(instance == null){
                    instance = new ChatDBHelper(uid,context);
                    instanceMap.put(uid,instance);
                }
            }
        }
        return instance;
    }

    public ChatDBHelper(String uid,@Nullable Context context) {
        super(context, uid + "_yushu_chat.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
