package com.ls.test.testutils.java_test;

import com.ls.comm_util_library.ISingleListener;

import java.util.List;

/**
 * @ClassName: ArticleContent
 * @Description:
 * @Author: ls
 * @Date: 2020/9/23 14:47
 */
public class ArticleContent {
    private List<ArticleContentItem> contentItems;

    public ArticleContent(List<ArticleContentItem> contentItems) {
        this.contentItems = contentItems;
    }

    public List<ArticleContentItem> getContentItems() {
        return contentItems;
    }

    public void forEach(ISingleListener<ArticleContentItem> action){
        for (ArticleContentItem item : contentItems){
            action.onValue(item);
        }
    }

    @Override
    public String toString() {
        return "ArticleContent{" +
                "content=" + contentItems +
                '}';
    }
}
