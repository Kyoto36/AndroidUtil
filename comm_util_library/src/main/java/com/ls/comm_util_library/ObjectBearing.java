package com.ls.comm_util_library;

/**
 * 对象承载类，可用在Rxjava等不能返回null，但是有需要null的时候使用
 * @param <T>
 */
public class ObjectBearing<T> {

    private T t;

    public ObjectBearing(T t){
        this.t = t;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "ObjectBearing{" +
                "t=" + t +
                '}';
    }
}
