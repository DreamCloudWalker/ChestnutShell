package com.dengjian.chestnutshell.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.sql.Ref;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtil {
    // for hot fix
    public static final String HOTFIX_DEX_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChestnutShell" + File.separator + "FixedDex" + File.separator;
    private static HashSet<File> sLoadedDex = new HashSet<>();

    static {
        sLoadedDex.clear();
    }

    public static void loadNoMainDex(Context context) {
        if (null == context){
            return ;
        }

        File fileDir = context.getDir("odex", Context.MODE_PRIVATE);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".dex") && !"classes.dex".equals(file.getName())) { // do not include main dex
                sLoadedDex.add(file);
            }
        }

        createDexClassLoader(context, fileDir);
    }

    private static void createDexClassLoader(Context context, File fileDir) {
        File fixedDexDir = new File(HOTFIX_DEX_PATH);
        if (!fixedDexDir.exists()) {
            fixedDexDir.mkdirs();
        }

        DexClassLoader myClassLoader; // load .class file by dir
        // dexPath, fixedDir, librarySearchPath, ClassLoader parent
        for (File dex : sLoadedDex) {
            myClassLoader = new DexClassLoader(dex.getAbsolutePath(), HOTFIX_DEX_PATH,
                    null, context.getClassLoader());
            hotFix(myClassLoader, context);
        }
    }

    private static void hotFix(DexClassLoader myClassLoader, Context context) {
        PathClassLoader sysClassLoader = (PathClassLoader) context.getClassLoader();

        try {
            Object myDexElements = ReflectUtil.getDexElements(ReflectUtil.getPathList(myClassLoader));
            Object sysDexElements = ReflectUtil.getDexElements(ReflectUtil.getPathList(sysClassLoader));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
