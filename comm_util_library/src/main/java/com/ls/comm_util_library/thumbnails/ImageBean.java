package com.ls.comm_util_library.thumbnails;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageBean implements Parcelable {
    private int id;
    private String name;
    private String path;
    private transient Uri uri;
    private String extend;

    public ImageBean(String extend){
        this.extend = extend;
    }

    public ImageBean(){}


    protected ImageBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        path = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        extend = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", uri=" + uri +
                ", extend='" + extend + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeParcelable(uri, flags);
        dest.writeString(extend);
    }
}
