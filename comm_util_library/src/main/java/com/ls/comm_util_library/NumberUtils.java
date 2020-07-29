package com.ls.comm_util_library;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
        else if(number >= 10000){
            DecimalFormat decimalFormat = new DecimalFormat("0.##");
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            return String.format("%s万",decimalFormat.format((float)number / 10000));
        }
        else{
            return number + "";
        }
    }

    public static int getPercent(long progress,long total){
        return getPPM(progress, total) / 100;
    }

    public static int getPPM(long progress,long total){
        return (int)(progress / (total / 10000.0));
    }
}
