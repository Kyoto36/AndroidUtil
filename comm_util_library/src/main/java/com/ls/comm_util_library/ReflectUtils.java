package com.ls.comm_util_library;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils {
    public static boolean reflectMethod(Object obj, String methodName, Object... args) {

        Class[] argClazzs = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argClazzs[i] = args[i].getClass();
        }
        Method method = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, argClazzs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(method != null) {
                method.setAccessible(true);
                break;
            }
        }
        if (method != null) {
            try {
                method.invoke(obj, args);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Object reflectStaticMethodResult(Class clazz, String methodName, Object... args) {
        try {
            Class[] paramClazz = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramClazz[i] = args[i].getClass();
            }
            Method method = clazz.getDeclaredMethod(methodName, paramClazz);
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object reflectMethodResult(Object obj, String methodName, Object... args) {
        try {
            Class[] paramClazz = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramClazz[i] = args[i].getClass();
            }
            Method method = obj.getClass().getDeclaredMethod(methodName, paramClazz);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取该对象所有属性的值
     * @return 属性名 -> 属性值，键值对
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Map<String, Object> getAllFieldValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //获取属性值
            try {
                //开启反射获取私有属性值
                fields[i].setAccessible(true);
                map.put(fields[i].getName(), fields[i].get(obj));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 获取单个属性值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj,String fieldName){
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
