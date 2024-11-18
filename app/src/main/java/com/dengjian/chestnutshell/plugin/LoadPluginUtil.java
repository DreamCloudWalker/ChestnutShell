package com.dengjian.chestnutshell.plugin;

import android.content.Context;
import android.os.storage.StorageManager;

import com.dengjian.common.utils.StorageUtil;

import java.lang.reflect.Field;

public class LoadPluginUtil {
    private static final String PLUGIN_DEX_PATH = StorageUtil.getPluginDir();

    public static void loadClass(Context context) {
        try {
            Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
