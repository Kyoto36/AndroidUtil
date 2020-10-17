package com.ls.test.testutils.java_test;

import com.google.gson.annotations.SerializedName;

public class UserItem extends User{
    public UserItem() {
    }

    public UserItem(User user) {
       fill(user);
    }

    private int hasNew;
    @SerializedName("is_follow")
    private int hasFollow;

    public boolean isNew(){
        return hasNew == 1;
    }

    public void setNew(boolean isNew){
        hasNew = isNew ? 1 : 2;
    }

    public int getHasNew() {
        return hasNew;
    }

    public void setHasNew(int hasNew) {
        this.hasNew = hasNew;
    }

    public boolean isFollow(){
        return hasFollow == 1;
    }

    public void setFollow(boolean follow){
        hasFollow = follow ? 1 : 2;
    }

    public int getHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(int hasFollow) {
        this.hasFollow = hasFollow;
    }

    @Override
    public String toString() {
        return super.toString() + " UserItem{" +
                "hasNew=" + hasNew +
                ", hasFollow=" + hasFollow +
                '}';
    }
}
