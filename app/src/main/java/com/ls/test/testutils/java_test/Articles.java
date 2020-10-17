package com.ls.test.testutils.java_test;


import java.util.List;

/**
 * @ClassName: Users
 * @Description:
 * @Author: ls
 * @Date: 2020/9/7 15:29
 */
public class Articles {
    private List<Article> articles;

    public Articles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "Articles{" +
                "articles=" + articles +
                '}';
    }
}
