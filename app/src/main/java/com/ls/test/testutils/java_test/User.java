package com.ls.test.testutils.java_test;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    @SerializedName("uid")
    private String id = "";
    private String nickname;
    @SerializedName("headurl")
    private String avatar;
    @SerializedName("gender")
    private int sex = -1;
    @SerializedName("birth")
    private String birthday;
    @SerializedName("signature")
    private String intro;
    @SerializedName("follownums")
    private long followNumber = -1;
    @SerializedName("fansnums")
    private long fansNumber = -1;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(long followNumber) {
        this.followNumber = followNumber;
    }

    public long getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(long fansNumber) {
        this.fansNumber = fansNumber;
    }

    public void fill(User user){
        if(user == null){
            return;
        }
        if(!TextUtils.isEmpty(user.getId())){
            id = user.getId();
        }
        if(!TextUtils.isEmpty(user.getNickname())){
            nickname = user.getNickname();
        }
        if(!TextUtils.isEmpty(user.getAvatar())){
            avatar = user.getAvatar();
        }
        if(user.getSex() == -1){
            sex = user.getSex();
        }
        if(!TextUtils.isEmpty(user.getBirthday())){
            birthday = user.getBirthday();
        }
        if(!TextUtils.isEmpty(user.getIntro())){
            intro = user.getIntro();
        }
        if(user.getFollowNumber() == -1){
            followNumber = user.getFollowNumber();
        }
        if(user.getFansNumber() == -1){
            fansNumber = user.getFansNumber();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex=" + sex +
                ", birthday='" + birthday + '\'' +
                ", intro='" + intro + '\'' +
                ", followNumber=" + followNumber +
                ", fansNumber=" + fansNumber +
                '}';
    }
}
