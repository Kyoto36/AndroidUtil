package com.ls.retrofit_library;

import java.io.IOException;

/**
 * @ClassName: ServerIOException
 * @Description:
 * @Author: ls
 * @Date: 2020/9/15 21:22
 */
public class ServerIOException extends IOException {
    private int code;

    public ServerIOException(int code){
        super();
        this.code = code;
    }

    public ServerIOException(int code,String message){
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
