package com.ls.test.testutils.java_test;


import java.util.Arrays;
import java.util.Map;

/**
 * @ClassName: ArticleContent
 * @Description:
 * @Author: ls
 * @Date: 2020/9/21 13:27
 */
public class ArticleContentItem {

    public enum Type{
        IMAGE,TEXT,LINK,NULL
    }

    private String[] contents;
    private Type type;
    private Map<String,Image> imageList;
    private Map<String, User> atList;

    public ArticleContentItem(Type type, String[] contents) {
        this.type = type;
        this.contents = contents;
    }

    public Type getType() {
        return type;
    }

    public String[] getContents() {
        return contents;
    }

    public Map<String, Image> getImageList() {
        return imageList;
    }

    public Map<String, User> getAtList() {
        return atList;
    }

    public void setDepend(Map<String, Image> imageList, Map<String, User> atList) {
        this.imageList = imageList;
        this.atList = atList;
    }

    @Override
    public String toString() {
        return "ArticleContentItem{" +
                "type=" + type +
                ", contents=" + Arrays.toString(contents) +
                '}';
    }
}
