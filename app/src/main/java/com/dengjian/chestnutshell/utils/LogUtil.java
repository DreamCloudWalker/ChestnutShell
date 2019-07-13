package com.dengjian.chestnutshell.utils;

public class LogUtil {
    public static final String TAG = "LogUtil";
    public static final boolean DEBUG_MODE = true; // Log switch

    /**
     * print verbose log
     *
     * @param tag
     * @param str
     */
    public static void v(String tag, String str) {
        if (DEBUG_MODE) {
            android.util.Log.v(tag, str);
        }
    }

    public static void v(String str) {
        if (DEBUG_MODE) {
            android.util.Log.v(TAG, str);
        }
    }

    /**
     * print debug log
     *
     * @param tag
     * @param str
     */
    public static void d(String tag, String str) {
        if (DEBUG_MODE) {
            android.util.Log.d(tag, str);
        }
    }

    public static void d(String str) {
        if (DEBUG_MODE) {
            android.util.Log.d(TAG, str);
        }
    }

    /**
     * print info log
     *
     * @param tag
     * @param str
     */
    public static void i(String tag, String str) {
        if (DEBUG_MODE) {
            android.util.Log.i(tag, str);
        }
    }

    public static void i(String str) {
        if (DEBUG_MODE) {
            android.util.Log.i(TAG, str);
        }
    }

    /**
     * print warning log
     *
     * @param tag
     * @param str
     */
    public static void w(String tag, String str) {
        android.util.Log.w(tag, str);
    }

    public static void w(String str) {
        android.util.Log.w(TAG, str);
    }

    /**
     * print error log
     *
     * @param tag
     * @param str
     */
    public static void e(String tag, String str) {
        android.util.Log.e(tag, str);
    }

    public static void e(String tag, String str, Throwable e) {
        android.util.Log.e(tag, str, e);
    }

    public static void e(String str) {
        android.util.Log.e(TAG, str);
    }
}
