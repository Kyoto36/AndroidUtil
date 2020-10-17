package com.ls.test.testutils.java_test;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: Article
 * @Description:
 * @Author: ls
 * @Date: 2020/9/4 18:00
 */
@Entity()
public class Article {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    @SerializedName("aid")
    private String id = "";
    private long time = -1;
    private String authorId = "";
    private String title;
    @SerializedName("des")
    private String contents;
    @SerializedName("image")
    private String firstImage;
    private int imageNum;
    private String images;
    private String ats;
    private String tags;
    @SerializedName("praisenums")
    private long praiseNumber = -1;
    @SerializedName("messagenums")
    private long commentNumber = -1;
    @SerializedName("sharenums")
    private long shareNumber = -1;
    @SerializedName("views")
    private long viewNumber = -1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAts() {
        return ats;
    }

    public void setAts(String ats) {
        this.ats = ats;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(long praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public long getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(long commentNumber) {
        this.commentNumber = commentNumber;
    }

    public long getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(long shareNumber) {
        this.shareNumber = shareNumber;
    }

    public long getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(long viewNumber) {
        this.viewNumber = viewNumber;
    }

    public void fill(Article article){
        if(!TextUtils.isEmpty(article.getId())){
            id = article.getId();
        }
        if(article.getTime() != -1){
            time = article.getTime();
        }
        if(!TextUtils.isEmpty(article.getAuthorId())){
            authorId = article.getAuthorId();
        }
        if(!TextUtils.isEmpty(article.getTitle())){
            title = article.getTitle();
        }
        if(!TextUtils.isEmpty(article.getContents())){
            contents = article.getContents();
        }
        if(!TextUtils.isEmpty(article.getFirstImage())){
            firstImage = article.getFirstImage();
        }
        if(article.getImageNum() != -1){
            imageNum = article.getImageNum();
        }
        if(!TextUtils.isEmpty(article.getImages())){
            images = article.getImages();
        }
        if(!TextUtils.isEmpty(article.getAts())){
            ats = article.getAts();
        }
        if(!TextUtils.isEmpty(article.getTags())){
            tags = article.getTags();
        }
        if(article.getPraiseNumber() == -1){
            praiseNumber = article.getPraiseNumber();
        }
        if(article.getCommentNumber() == -1){
            commentNumber = article.getCommentNumber();
        }
        if(article.getShareNumber() == -1){
            shareNumber = article.getShareNumber();
        }
        if(article.getViewNumber() == -1){
            viewNumber = article.getViewNumber();
        }
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", time=" + time +
                ", authorId='" + authorId + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", firstImage='" + firstImage + '\'' +
                ", imageNum=" + imageNum +
                ", images='" + images + '\'' +
                ", ats='" + ats + '\'' +
                ", tags='" + tags + '\'' +
                ", praiseNumber=" + praiseNumber +
                ", commentNumber=" + commentNumber +
                ", shareNumber=" + shareNumber +
                ", viewNumber=" + viewNumber +
                '}';
    }
}
