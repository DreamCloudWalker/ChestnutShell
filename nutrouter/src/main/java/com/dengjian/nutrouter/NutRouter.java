package com.dengjian.nutrouter;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class NutRouter {
    private static volatile NutRouter mInstance = null;
    private Map<String, Class<? extends Activity>> mActivityMap = new HashMap<>();

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
}
