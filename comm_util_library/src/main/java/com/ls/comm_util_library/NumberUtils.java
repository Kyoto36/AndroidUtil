package com.ls.comm_util_library;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class NumberUtils {

    /**
     * 格式化数字，一般用于点赞数
     * @param number 需要格式化的数字
     * @return
     */
    public static String numberFormat(long number){
        if(number <= 0){
            return "";
        }
        else if(number >= 100000000){
            return String.format("%s亿",toFixed((float)number / 100000000,2));
        }
        else if(number >= 10000){
            return String.format("%s万",toFixed((float)number / 10000,2));
        }
        else{
            return number + "";
        }
    }

    /**
     * 格式化数字，一般用于点赞数
     * @param number 需要格式化的数字
     * @return
     */
    public static String numberFormat(long number,String defaultStr){
        if(number <= 0){
            return defaultStr;
        }
        else if(number >= 10000){
            return String.format("%s万",toFixed((float)number / 10000,2));
        }
        else{
            return number + "";
        }
    }

    /**
     * 保留几位小数
     * @param number double数
     * @param digit 几位小数
     * @return
     */
    public static String toFixed(float number,int digit){
        double d = number;
        return toFixed(d, digit);
    }

    /**
     * 保留几位小数
     * @param number double数
     * @param digit 几位小数
     * @return
     */
    public static String toFixed(double number,int digit){
        if(digit <= 0){
            return "" + (int)number;
        }
        StringBuilder sb = new StringBuilder("#.");
        for (int i = 0; i < digit; i++){
            sb.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
        return decimalFormat.format(number);
    }

    /**
     * 获取百分比，保留率单精度小数位
     * @param progress
     * @param total
     * @return
     */
    public static float getPercentFloat(long progress,long total){
        float ppm = getPPMFloat(progress,total);
        return ppm / 100;
    }

    /**
     * 获取百分比
     * @param progress 当前进度
     * @param total 中大小
     * @return 百分数
     */
    public static int getPercent(long progress,long total){
        return getPPM(progress, total) / 100;
    }

    /**
     * 获取万分比
     * @param progress 当前进度
     * @param total 中大小
     * @return 万分数
     */
    public static int getPPM(long progress,long total){
        return (int)(progress / (total / 10000.0));
    }

    /**
     * 获取万分比，保留率单精度小数位
     * @param progress 当前进度
     * @param total 中大小
     * @return 万分数
     */
    public static float getPPMFloat(long progress,long total){
        return (int)(progress / (total / 10000.0));
    }

    /**
     * 判断是否是整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str){
        if(TextUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 是否是数字，包括小数
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        return str.matches("-?[0-9]+.？[0-9]*");
    }
}
