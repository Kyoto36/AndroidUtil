package com.ls.test.testutils.java_test;


import java.util.List;

/**
 * @ClassName: Users
 * @Description:
 * @Author: ls
 * @Date: 2020/9/7 15:29
 */
public class Topics {
    private List<Topic> topicList;

    public Topics(List<Topic> topicList) {
        this.topicList = topicList;
    }

    public List<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }

    @Override
    public String toString() {
        return "Topics{" +
                "topicList=" + topicList +
                '}';
    }
}
