package com.ls.comm_util_library;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class AppUtils {
    /**
     * 获取app版本号
     * @param context
     * @return 版本号versionname，不是versioncode
     */
    public static String getAppVerCode(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取app名称
     * @param context
     * @param pID 进程ID
     * @return
     */
    public static String getAppName(Context context,int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 获取application下的matedata数据（String数据）
     * @param context
     * @param keyName
     * @return
     */
    public static String getMetaDataStringFromApplication(Context context,String keyName) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取application下的matedata数据(int数据)
     * @param context
     * @param keyName
     * @return
     */
    public static int getMetaDataIntFromApplication(Context context,String keyName) {
        int value = -1;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getInt(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取application下的matedata数据(long数据)
     * @param context
     * @param keyName
     * @return
     */
    public static long getMetaDataLongFromApplication(Context context,String keyName) {
        long value = -1;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getLong(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 退出app
     */
    public static void exitApp(Context context){
        exitApp(context,"双击退出");
    }

    private static long sExitTime;

    public static void exitApp(Context context,String msg){
        if(System.currentTimeMillis() - sExitTime > 2000){
            Util.Companion.toast(context,msg);
            sExitTime = System.currentTimeMillis();
        }
        else{
            sExitTime = 0;
            if(context instanceof Activity){
                UtilActivityManager.exitApp((Activity)context);
            }
            else {
                UtilActivityManager.exitApp();
            }
        }
    }

}
