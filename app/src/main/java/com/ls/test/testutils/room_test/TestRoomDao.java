package com.ls.test.testutils.room_test;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * @ClassName: TestRoomDao
 * @Description:
 * @Author: ls
 * @Date: 2020/9/23 18:42
 */
@Dao
public abstract class TestRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(TestRoom testRoom);

    @Delete
    public abstract int delete(TestRoom testRoom);

    @Query("select * from TestRoom where userId = :userId and type = :type and typeContentId = :typeContentId limit 1")
    public abstract TestRoom query(String userId,int type,String typeContentId);

    @Query("select * from TestRoom")
    public abstract List<TestRoom> listAll();
}
