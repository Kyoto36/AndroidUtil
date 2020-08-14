package com.ls.comm_util_library.thumbnails;

public class ThumbnailBean {
    private int id;
    private int imageId;
    private String data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ThumbnailBean{" +
                "id=" + id +
                ", imageId=" + imageId +
                ", data='" + data + '\'' +
                '}';
    }
}
