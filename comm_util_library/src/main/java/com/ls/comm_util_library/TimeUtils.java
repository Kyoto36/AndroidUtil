package com.ls.comm_util_library;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimeUtils {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LACK_SEC_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
    public static final String LACK_SEC_FORMAT_CHINESE = "yyyy年MM月dd日 HH:mm";
    public static final String LACK_YEAR_FORMAT = "MM-dd HH:mm";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    public static final String MONTH_DAY_TIME_FORMAT = "MM-dd HH:mm";
    public static final String MONTH_DAY_FORMAT_CHINESE = "MM月dd日";
    public static final String MONTH_DAY_TIME_FORMAT_CHINESE = "MM月dd日 HH:mm";

    public static final int SEC = 1000;
    public static final int MIN = SEC * 60;
    public static final int HOUR = MIN * 60;
    public static final int DAY = HOUR * 24;

    /**
     * 获取几年的毫秒数
     * @param year 几年 1 2 3
     * @return
     */
    public static long year2Millis(int year){
        return year * 365L * 24 * 60 * 60 * 1000;
    }

    /**
     * 获取几年前的毫秒数时间
     * @param year 几年 1 2 3
     * @return
     */
    public static long yearsAgo(int year){
        long millis = System.currentTimeMillis() - year2Millis(year);
        return millis;
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

    public static String sec2String(long sec,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(sec * 1000));
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
        else if(diff / 60 / 60 / 24 / 7 <= 0){
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

    public static String dateFlashback(long sec,boolean showTime){
        long millis =  sec * 1000;
        long currSec = new Date().getTime() / 1000;
        long diff = currSec - sec;
        Calendar datetime = Calendar.getInstance();
        datetime.setTime(new Date(millis));
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int dayDiff = now.get(Calendar.DAY_OF_YEAR) - datetime.get(Calendar.DAY_OF_YEAR);
        int yearDiff = now.get(Calendar.YEAR) - datetime.get(Calendar.YEAR);
        if(diff / 60 <= 0){
            return "刚刚";
        }
        else if(diff / 60 / 60 <= 0){
            return (diff / 60) + "分钟前";
        }
        else if(diff / 60 / 60 / 24 <= 0){
            return (diff / 60 / 60) + "小时前";
        }
//        else if(diff / 60 / 60 / 24 / 2 <= 0){
        else if(dayDiff == 1){
            return "昨天" + millis2String(millis,TIME_FORMAT);
        }
//        else if(diff / 60 / 60 / 24 / 365 <= 0){
        else if(yearDiff > 0){
            return millis2String(millis,showTime ? LACK_SEC_FORMAT_CHINESE : DEFAULT_DATE_FORMAT_CHINESE );
//            return millis2String(millis,showTime ? MONTH_DAY_TIME_FORMAT : MONTH_DAY_FORMAT);
        }
        else{
//            return millis2String(millis,showTime ? LACK_SEC_FORMAT : DEFAULT_DATE_FORMAT );
            return millis2String(millis,showTime ? MONTH_DAY_TIME_FORMAT_CHINESE : MONTH_DAY_FORMAT_CHINESE);
        }
    }

    public static String millis2Minute(long millis){
        int sec = (int) (millis/1000);
        int minute = sec / 60;
        sec = sec % 60;
        return (minute < 10 ? "0" + minute : "" + minute) + ":" + (sec < 10 ? "0" + sec : "" + sec);
    }

    public static String sec2String(long sec){
        return time2String(new Date(sec * 1000));
    }

    public static String time2String(long time){
        return time2String(new Date(time));
    }

    public static String time2String(Date time){
        Calendar datetime = Calendar.getInstance();
        datetime.setTime(time);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int delay = now.get(Calendar.DAY_OF_YEAR) - datetime.get(Calendar.DAY_OF_YEAR);
        if(delay == 0){
            return date2String(time, TIME_FORMAT);
        }
        if(delay == 1){
            return "昨天 " + date2String(time, TIME_FORMAT);
        }
        else{
            return date2String(time,LACK_SEC_FORMAT);
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

    /**
     * 获取今日零点零时零分零秒时间戳
     * @return 毫秒
     */
    public static long getToDay0(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String now = sdf.format(date);
        try
        {
            Date newDate = sdf.parse(now);
            return newDate.getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 对应 <code>getTimeArray(long millis)</code>返回的数组中年月日对应的下标
     * @see #getTimeArray(long)
     */
    public static int YEAR_INDEX = 0;
    public static int MONTH_INDEX = 1;
    public static int DAY_INDEX = 2;
    public static int HOUR_INDEX = 3;
    public static int MINUTE_INDEX = 4;
    public static int SECOND_INDEX = 5;
    public static int WEEK_INDEX = 6;

    /**
     * 获取时间戳对应的年、月、日、时、分、秒、周
     * @param millis 毫秒时间戳
     * @return
     */
    public static int[] getTimeArray(long millis){
        Calendar datetime = Calendar.getInstance();
        datetime.setTime(new Date(millis));
        return new int[]{
                // 年
                datetime.get(Calendar.YEAR),
                // 月 0 = 一月，所以要 +1
                datetime.get(Calendar.MONTH) + 1,
                // 日
                datetime.get(Calendar.DAY_OF_MONTH),
                // 时
                datetime.get(Calendar.HOUR),
                // 分
                datetime.get(Calendar.MINUTE),
                // 秒
                datetime.get(Calendar.SECOND),
                // 周
                datetime.get(Calendar.WEEK_OF_YEAR)
        };
    }

}
