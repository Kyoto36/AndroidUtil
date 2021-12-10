package com.ls.comm_util_library;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.internal.LinkedHashTreeMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * activity管理类
 *
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

    public static void init(Application application){
        get().registerActivityListener(application);
    }

    public void registerActivityListener(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /**
                     *  监听到 Activity创建事件 将该 Activity 加入list
                     */
                    addActivity(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {
                    /**
                     * 当一个Activity可见并聚焦时，设置为顶部Activity
                     */
                    setTopActivity(activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (isExistActivity(activity)) {
                        /**
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        removeActivity(activity);
                    }
                }
            });
        }
    }

    /**
     * 设置最上层的activity
     * android 14 以下调用
     * @param activity
     */
    public static void setTopActivity(Activity activity){
        List<Activity> list = get().mActivities;
        if(activity == list.get(list.size() - 1)){
            return;
        }
        if(list.contains(activity)){
            list.remove(activity);
        }
        list.add(activity);
    }

    /**
     * 添加Activity
     * android 14 以下调用
     * @param activity
     */
    public static void addActivity(Activity activity){
        get().mActivities.add(activity);
    }

    /**
     * 移除Activity
     * android 14 以下调用
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
        List<Activity> list = get().mActivities;
        List<Activity> activities = new ArrayList<>();
        for (Activity activity : list){
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
        List<Activity> list = get().mActivities;
        List<Activity> finishActivities = new ArrayList<>();
        for (Activity activity: list){
            if(!activity.getClass().equals(clazz) && !activity.isFinishing()){
                finishActivities.add(activity);
                activity.finish();
            }
        }
        list.removeAll(finishActivities);
    }

    /**
     * finish所有Activity 除了activity类型之外
     * @param activity
     */
    public static void finishOther(Activity activity){
        List<Activity> list = get().mActivities;
        List<Activity> finishActivities = new ArrayList<>();
        for (Activity item: list){
            if(item != activity && !item.isFinishing()){
                finishActivities.add(activity);
                item.finish();
            }
        }
        list.removeAll(finishActivities);
    }

    /**
     * finish所有Activity，除了首个未finish的activity
     * @return
     */
    public static Activity finishResultTop(){
        List<Activity> list = get().mActivities;
        Activity activity = null;
        while (list.size() > 0) {
            activity = list.get(list.size() - 1);
            if(activity != null && !activity.isFinishing()){
                break;
            }
        }
        for (Activity item: list){
            if(!item.isFinishing()){
                item.finish();
            }
        }
        list.clear();
        return activity;
    }

    /**
     * 获取最上层的activity
     * @return
     */
    public static Activity getTopActivity(){
        List<Activity> list = get().mActivities;
        Activity activity = null;
        while (list.size() > 0) {
            activity = list.get(list.size() - 1);
            if(activity != null && !activity.isFinishing()){
                break;
            }
            else{
                list.remove(activity);
            }
        }
        return activity;
    }

    /**
     * finish所有Activity，循环遍历所有正在运行的activity并finish
     */
    public static void exitAllActivity(){
        for (Activity activity : get().mActivities){
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 退出所有activity，循环遍历所有正在运行的activity并finish
     * 但是把当前activity留到最后finish
     * @param activity 当前activity
     */
    public static void exitAllActivity(Activity activity){
        List<Activity> list = get().mActivities;
        for (Activity item : list){
            if(item != activity && !item.isFinishing()) {
                item.finish();
            }
        }
        if(!activity.isFinishing()) {
            activity.finish();
        }
        list.clear();
    }

    private final List<Activity> mActivities = new ArrayList<>();
}
