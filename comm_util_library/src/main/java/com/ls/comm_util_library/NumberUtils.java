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
     * 数字上限显示效果 99+
     * @param number
     * @param limit
     * @return
     */
    public static String numberLimit(long number,int limit){
        return number > limit ? limit + "+": number + "";
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

    /**
     * 简体中文数字
     */
    public static String[] SIMPLE_CHINESE_NUM = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    public static String[] SIMPLE_CHINESE_UNIT = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};

    /**
     * 获取不带单位的中文数字
     * @param num
     * @return
     */
    public static String getSimpleChineseUnUnit(int num){
        int src = num;
        int remainder = 0;
        StringBuilder sb = new StringBuilder();
        while(src > 0){
            remainder = src % 10;
            sb.insert(0,SIMPLE_CHINESE_NUM[remainder]);
            src = src / 10;
        }
        return sb.toString();
    }

    /**
     * 获取带单位的中文数字
     * @param num
     * @return
     */
    public static String getSimpleChinese(int num){
        int src = num;
        int remainder = 0;
        StringBuilder sb = new StringBuilder();
        int digits = 0;
        while(src > 0){
            remainder = src % 10;
            sb.insert(0,SIMPLE_CHINESE_UNIT[digits]);
            sb.insert(0,SIMPLE_CHINESE_NUM[remainder]);
            src = src / 10;
            digits++;
        }
        return sb.toString().replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }
}
