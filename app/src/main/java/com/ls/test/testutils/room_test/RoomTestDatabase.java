package com.ls.test.testutils.room_test;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @ClassName: RoomTestDatabase
 * @Description:
 * @Author: ls
 * @Date: 2020/9/23 18:39
 */
@Database(entities = {TestRoom.class},version = 1)
public abstract class RoomTestDatabase extends RoomDatabase {
    private static RoomTestDatabase instance;

    public static RoomTestDatabase get(Context context){
        if(instance == null){
            synchronized (RoomDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context,RoomTestDatabase.class,"room_test.db")
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract TestRoomDao testRoomDao();
}
