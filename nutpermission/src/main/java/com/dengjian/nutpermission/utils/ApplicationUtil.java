package com.dengjian.nutpermission.utils;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

/**
 * 反射获得当前App线程的Application context
 */
public class ApplicationUtil {
    private static Application mApplication;

    public static Application getApplication() {
        if (mApplication != null) {
            return mApplication;
        } else {
            mApplication = getApplicationByReflect();
        }
        return mApplication;
    }

    private static Application getApplicationByReflect() {
        try {  // TODO check android 10
            return (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null, (Object[]) null);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("application is null");
    }
}
