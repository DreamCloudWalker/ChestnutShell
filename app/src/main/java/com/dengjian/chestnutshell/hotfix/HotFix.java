package com.dengjian.chestnutshell.hotfix;

import android.app.Application;

import com.dengjian.chestnutshell.utils.ReflectUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HotFix {

    /**
     * 执行热修复
     * 1. 获取当前的PathClassLoader;
     * 2. 反射获取DexPathList属性对象pathList;
     * 3. 反射修改pathList的dexElements
     *   3.1 把补丁包patch.dex转换成Element[],通过反射执行源码DexPathList中的makePathElements
     *   3.2 获得pathList的dexElements属性（old）
     *   3.3 patch+old合并，并反射赋值给pathList的dexElements
     * @param patchPath 补丁包路径
     */
    public static void installPatch(Application application, String patchPath) {
        File patch = new File(patchPath);
        if (!patch.exists()) {
            return ;
        }
        List<File> files = new ArrayList<>();
        files.add(patch);
        File filesDir = application.getFilesDir();

        ClassLoader classLoader = application.getClassLoader(); // 1

        Field pathListField = ReflectUtil.getField(classLoader, "pathList");    // 2

        try {   // 3
            Object pathList = pathListField.get(classLoader);

            // 3.1
            Method makePathElements = ReflectUtil.getMethod(pathList, "makePathElements",
                    List.class, File.class, List.class);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();

            Object[] patchElements = (Object[]) makePathElements.invoke(pathList, files, filesDir,
                    suppressedExceptions);

            // 3.2
            Field dexElementsField = ReflectUtil.getField(pathList, "dexElements");
            Object[] oldElements = (Object[]) dexElementsField.get(pathList);

            // 3.3
            Object[] newElements = (Object[]) Array.newInstance(patchElements.getClass().getComponentType(),
                    patchElements.length + oldElements.length);
            System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
            System.arraycopy(oldElements, 0, newElements, patchElements.length, oldElements.length);

            dexElementsField.set(pathList, newElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
