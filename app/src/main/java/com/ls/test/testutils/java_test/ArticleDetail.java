package com.ls.test.testutils.java_test;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ArticleDetail extends Article {

    public ArticleDetail() {
    }

    public ArticleDetail(Article article) {
        fill(article);
    }

    @SerializedName("member")
    private UserItem userItem;

    // Article中的contents是存入数据库的，这个是接收服务端的
    private ArticleContent content;
    @SerializedName("image_list")
    private Map<String,Image> imageList;
    @SerializedName("at_list")
    private JsonElement atList;
    @SerializedName("tag_list")
    private Topics tagList;
    @SerializedName("is_praise")
    private int hasPraise;

    public UserItem getUserItem() {
        return userItem;
    }

    public void setUserItem(UserItem userItem) {
        this.userItem = userItem;
    }

    public ArticleContent getContent() {
        return content;
    }

    public void setContent(ArticleContent content) {
        this.content = content;
    }

    public Map<String, Image> getImageList() {
        return imageList;
    }

    public void setImageList(Map<String, Image> imageList) {
        this.imageList = imageList;
    }

    public JsonElement getAtList() {
        return atList;
    }

    public void setAtList(JsonElement atList) {
        this.atList = atList;
    }

    public Topics getTagList() {
        return tagList;
    }

    public void setTagList(Topics tagList) {
        this.tagList = tagList;
    }

    public void setPraise(boolean praise){
        setHasPraise(praise ? 1 : 2);
    }

    public boolean isPraise(){
        return hasPraise == 1;
    }

    public int getHasPraise() {
        return hasPraise;
    }

    public void setHasPraise(int hasPraise) {
        this.hasPraise = hasPraise;
    }

    @Override
    public String toString() {
        return super.toString() + " ArticleDetail{" +
                "userItem=" + userItem +
                ", content=" + content +
                ", imageList=" + imageList +
                ", atList=" + atList +
                ", tagList=" + tagList +
                ", hasPraise=" + hasPraise +
                '}';
    }
}
