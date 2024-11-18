package com.dengjian.common.utils;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class StorageUtil {
    private final static String TAG = StorageUtil.class.getName();

    private static String APP_LOG_DIR;
    private static String APP_TMP_DIR;
    private static String APP_IMAGE_DIR;
    private static String APP_CRASH_INFO_DIR;
    private static String APP_PLUGIN_DIR;
    private static String APP_PATCHES;

    public static void init(Application application) {
        String internalRoot;
        String exStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(exStorageState)) {
            File baseDirFile = application.getExternalFilesDir(null);
            if (null == baseDirFile) {
                internalRoot = application.getFilesDir().getAbsolutePath();
            } else {
                internalRoot = baseDirFile.getAbsolutePath();
            }
        } else {
            internalRoot = application.getFilesDir().getAbsolutePath();
        }

        APP_LOG_DIR = internalRoot + "/logs";
        APP_TMP_DIR = internalRoot + "/tmp";
        APP_IMAGE_DIR = internalRoot + "/images";
        APP_CRASH_INFO_DIR = internalRoot + "/crash";
        APP_PLUGIN_DIR = internalRoot + "/plugin";
        APP_PATCHES = internalRoot + "/patches";
    }

    public static String getAppLogDir() {
        return APP_LOG_DIR;
    }

    public static String getAppTmpDir() {
        return APP_TMP_DIR;
    }

    public static String getAppImageDir() {
        return APP_IMAGE_DIR;
    }

    public static String getCrashInfoDir() {
        return APP_CRASH_INFO_DIR;
    }

    public static String getPluginDir() {
        return APP_PLUGIN_DIR;
    }

    public static String getPatchesDir() {
        return APP_PATCHES;
    }

    private static void checkFolder(File f) {
        if (!f.isDirectory()) {
            if (f.exists()) {
                boolean result = f.delete();
                if (!result) {
                    Log.e(TAG, "delete file failed");
                }
            }
            boolean result = f.mkdirs();
            if (!result) {
                Log.e(TAG, "create dir failed");
            }
        }
    }
}
