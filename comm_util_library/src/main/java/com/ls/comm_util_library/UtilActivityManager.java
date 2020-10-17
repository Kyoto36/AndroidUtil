package com.ls.comm_util_library;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * activity管理类
 * 但是要配合BaseUtilActivity使用，所有activity都需要继承与BaseUtilActivity
 */
public class UtilActivityManager {
    private static UtilActivityManager sInstance;

    /**
     * 双重校验锁单例
     * @return
     */
    public static UtilActivityManager get(){
        if(sInstance == null) {
            synchronized (UtilActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new UtilActivityManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加Activity
     * @param activity
     */
    public static void addActivity(Activity activity){
        get().mActivities.add(activity);
    }

    /**
     * 移除Activity
     * @param activity
     */
    public static void removeActivity(Activity activity){
        get().mActivities.remove(activity);
    }

    /**
     * 是否存在当前Activity
     * @param activity
     * @return
     */
    public static boolean isExistActivity(Activity activity){
        return get().mActivities.contains(activity);
    }

    /**
     * 该Class有几个Activity实例
     * @param clazz
     * @return
     */
    public static List<Activity> existActivity(Class<? extends Activity> clazz){
        List<Activity> activities = new ArrayList<>();
        for (Activity activity : get().mActivities){
            if(activity.getClass().equals(clazz)){
                activities.add(activity);
            }
        }
        return activities;
    }

    /**
     * 是否只剩下一个activity
     * @return
     */
    public static boolean isSingleActivity(){
        return get().mActivities.size() == 1;
    }

    /**
     * finish所有Activity 除了clazz类型之外
     * @param clazz
     */
    public static void finishOther(Class<? extends Activity> clazz){
        for (Activity activity: get().mActivities){
            if(!activity.getClass().equals(clazz) && !activity.isFinishing()){
                activity.finish();
            }
        }
    }

    /**
     * finish所有Activity 除了activity类型之外
     * @param activity
     */
    public static void finishOther(Activity activity){
        for (Activity item: get().mActivities){
            if(item != activity && !item.isFinishing()){
                item.finish();
            }
        }
    }

    /**
     * 退出app，循环遍历所有正在运行的activity并finish
     */
    public static void exitApp(){
        for (Activity activity : get().mActivities){
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 退出app，循环遍历所有正在运行的activity并finish
     * 但是把当前activity留到最后finish
     * @param activity 当前activity
     */
    public static void exitApp(Activity activity){
        for (Activity item : get().mActivities){
            if(item != activity && !item.isFinishing()) {
                item.finish();
            }
        }
        if(!activity.isFinishing()) {
            activity.finish();
        }
    }

    private HashSet<Activity> mActivities = new HashSet<>();
}
