package com.ls.comm_util_library;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LACK_SEC_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String LACK_YEAR_FORMAT = "MM-dd HH:mm";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    public static final String MONTH_DAY_TIME_FORMAT = "MM-dd HH:mm";

    public static long year2Millis(int year){
        return year * 365L * 24 * 60 * 60 * 1000;
    }


    public static String date2String(Date date){
        return date2String(date, DEFAULT_FORMAT);
    }

    public static String date2String(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String millis2String(long millis){
        return millis2String(millis, DEFAULT_FORMAT);
    }

    public static String millis2String(long millis,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(millis));
    }

    public static Date string2Date(String str){
        return string2Date(str, DEFAULT_FORMAT);
    }

    public static Date string2Date(String str,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        return dateFormat.parse(str,pos);
    }

    public static String dateFlashback(long millis){
        long sec = millis / 1000;
        long currSec = new Date().getTime() / 1000;
        long diff = currSec - sec;
        if(diff / 60 <= 0){
            return "刚刚";
        }
        else if(diff / 60 / 60 <= 0){
            return (diff / 60) + "分钟前";
        }
        else if(diff / 60 / 60 / 24 <= 0){
            return (diff / 60 / 60) + "小时前";
        }
        else if(diff / 60 / 60 / 24 / 4 <= 0){
            return (diff / 60 / 60 / 24) + "天前";
        }
        else if(diff / 60 / 60 / 24 / 30 <= 0){
            return (diff / 60 / 60 / 24 / 7) + "周前";
        }
        else if(diff / 60 / 60 / 24 / 365 <= 0){
            return (diff / 60 / 60 / 24 / 30) + "个月前";
        }
        else{
            return millis2String(millis);
        }
    }

    public static String dateFlashback(Date date){
        return dateFlashback(date.getTime());

    }

    public static String dateFlashback(long millis,boolean showTime){
        long sec = millis / 1000;
        long currSec = new Date().getTime() / 1000;
        long diff = currSec - sec;
        if(diff / 60 <= 0){
            return "刚刚";
        }
        else if(diff / 60 / 60 <= 0){
            return (diff / 60) + "分钟前";
        }
        else if(diff / 60 / 60 / 24 <= 0){
            return (diff / 60 / 60) + "小时前";
        }
        else if(diff / 60 / 60 / 24 / 2 <= 0){
            return "昨天" + millis2String(millis,TIME_FORMAT);
        }
        else if(diff / 60 / 60 / 24 / 365 <= 0){
            return millis2String(millis,showTime ? MONTH_DAY_FORMAT : MONTH_DAY_TIME_FORMAT);
        }
        else{
            return millis2String(millis,showTime ? DEFAULT_DATE_FORMAT : DEFAULT_DATE_FORMAT);
        }
    }

    public static String millis2Minute(long millis){
        int sec = (int) (millis/1000);
        int minute = sec / 60;
        sec = sec % 60;
        return (minute < 10 ? "0" + minute : "" + minute) + ":" + (sec < 10 ? "0" + sec : "" + sec);
    }

    public static String commentTime2String(Date replyTime){
        Calendar datetime = Calendar.getInstance();
        datetime.setTime(replyTime);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int delay = now.get(Calendar.DAY_OF_YEAR) - datetime.get(Calendar.DAY_OF_YEAR);
        if(delay == 0){
            return date2String(replyTime, TIME_FORMAT);
        }
        if(delay == 1){
            return "昨天 " + date2String(replyTime, TIME_FORMAT);
        }
        else{
            return date2String(replyTime);
        }
    }

    public static boolean isLeapYear(int year){
        if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
            return true;
        }
        return false;
    }

    public static List<Integer> DAYS_31_MONTH = Arrays.asList(1,3,5,7,8,10,12);
    public static List<Integer> DAYS_30_MONTH = Arrays.asList(4,6,9,11);

    public static int getDays(int year,int month){
        if(month == 2){
            if(isLeapYear(year)) return 29;
            else return 28;
        }
        else if(DAYS_31_MONTH.contains(month)) return 31;
        else return 30;
    }
}
