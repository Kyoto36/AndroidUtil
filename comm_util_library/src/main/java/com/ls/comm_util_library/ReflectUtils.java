package com.ls.comm_util_library;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static boolean reflectMethod(Object obj, String methodName, Object... args){
        try {
            Class[] clazz = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                clazz[i] = args[i].getClass();
            }
            Method method = obj.getClass().getDeclaredMethod(methodName, clazz);
            method.setAccessible(true);
            method.invoke(obj, args);
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object reflectStaticMethodResult(Class clazz, String methodName, Object... args){
        try {
            Class[] paramClazz = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramClazz[i] = args[i].getClass();
            }
            Method method = clazz.getDeclaredMethod(methodName, paramClazz);
            method.setAccessible(true);
            return method.invoke( null, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object reflectMethodResult(Object obj, String methodName, Object... args){
        try {
            Class[] paramClazz = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramClazz[i] = args[i].getClass();
            }
            Method method = obj.getClass().getDeclaredMethod(methodName, paramClazz);
            method.setAccessible(true);
            return method.invoke( obj, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
