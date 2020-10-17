package com.ls.test.testutils.java_test;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Topic {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    @SerializedName("tid")
    private String id = "";
    @SerializedName("title")
    private String name;
    @SerializedName("des")
    private String description;
    private String icon;

    public Topic() {
    }

    @Ignore
    public Topic(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void fill(Topic topic){
        if(TextUtils.isEmpty(id)){
            id = topic.getId();
        }
        if(name == null){
            name = topic.getName();
        }
        if(description == null){
            description = topic.getDescription();
        }
        if(icon == null){
            icon = topic.getIcon();
        }
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
