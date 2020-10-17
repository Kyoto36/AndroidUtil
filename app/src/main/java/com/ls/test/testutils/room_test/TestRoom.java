package com.ls.test.testutils.room_test;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * @ClassName: TestRoom
 * @Description:
 * @Author: ls
 * @Date: 2020/9/23 18:36
 */
@Entity(primaryKeys = {"userId","type","typeContentId"})
public class TestRoom {
    @NonNull
    private String userId;
    @NonNull
    private int type;
    @NonNull
    private String typeContentId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeContentId() {
        return typeContentId;
    }

    public void setTypeContentId(String typeContentId) {
        this.typeContentId = typeContentId;
    }

    @Override
    public String toString() {
        return "TestRoom{" +
                "userId='" + userId + '\'' +
                ", type=" + type +
                ", typeContentId='" + typeContentId + '\'' +
                '}';
    }
}
