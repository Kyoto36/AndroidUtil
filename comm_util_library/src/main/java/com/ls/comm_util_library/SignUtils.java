package com.ls.comm_util_library;

import android.text.TextUtils;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SignUtils {

    public static final String MD5 = "md5";

    public static String signMD5(String unSign){
       return Md5Code.getMD5Code(unSign);
    }
}
