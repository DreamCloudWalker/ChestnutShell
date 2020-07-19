package com.dengjian.nutrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import dalvik.system.DexFile;

public class NutRouter {
    private static volatile NutRouter mInstance = null;
    private Map<String, Class<? extends Activity>> mActivityMap = new HashMap<>();
    private Context mContext = null;

    private NutRouter() {
    }

    public static NutRouter getInstance() {
        if (null == mInstance) {
            synchronized (NutRouter.class) {
                if (null == mInstance) {
                    mInstance = new NutRouter();
                }
            }
        }

        return mInstance;
    }

    public void putActivity(String key, Class<? extends Activity> value) {
        mActivityMap.put(key, value);
    }

    public void init(Context context) {
        mContext = context;
        List<String> classNameList = getClassName("com.dengjian.router");
        if (null != classNameList) {
            for (String className : classNameList) {
                try {
                    Class<?> aClazz = Class.forName(className);
                    // 判断这个class是否实现IRouter
                    if (IRouter.class.isAssignableFrom(aClazz)) {
                        IRouter iRouter = (IRouter) aClazz.newInstance();
                        iRouter.putActivity();
                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据包名获取包下面所有类名
     * @param packageName
     * @return
     */
    private @Nullable List<String> getClassName(String packageName) {
        List<String> ret = null;
        // 根据上下文获取当前应用apk的完整路径
        try {
            // 获取当前应用包的信息类
            ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0);
            String sourceDir = applicationInfo.sourceDir;   // APK最终存储路径
            DexFile dexFile = new DexFile(sourceDir);       // APK目录对象
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                // 每个目录或文件名（带包名）
                String fileName = entries.nextElement();
                if (fileName.contains(packageName)) {
                    if (null == ret) {
                        ret = new ArrayList<>();
                    }
                    ret.add(fileName);
                }
            }
        } catch (PackageManager.NameNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void startActivity(String key, Bundle bundle) { // TODO add 启动模式参数
        Class<? extends Activity> aClass = mActivityMap.get(key);
        if (null != aClass) {
            Intent intent = new Intent(mContext, aClass);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            mContext.startActivity(intent);
        }
    }
}
