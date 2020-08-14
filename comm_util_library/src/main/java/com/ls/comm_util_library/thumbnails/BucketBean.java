package com.ls.comm_util_library.thumbnails;

import java.util.List;

public class BucketBean {
    private int id;
    private String name;
    private List<Integer> imageIds;
    private List<ThumbnailBean> thumbnails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    public List<ThumbnailBean> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<ThumbnailBean> thumbnails) {
        this.thumbnails = thumbnails;
    }

    @Override
    public String toString() {
        return "BucketBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageIds=" + imageIds +
                ", thumbnails=" + thumbnails +
                '}';
    }
}
