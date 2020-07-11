package com.dengjian.chestnutshell.databus;

import android.arch.lifecycle.MutableLiveData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LiveDataBusBeta {
    private final Map<String, MutableLiveData<Object>> mLiveDataBusBeta;

    private LiveDataBusBeta() {
        mLiveDataBusBeta = new ConcurrentHashMap<>();
    }

    private static class SingleHolder {
        private static final LiveDataBusBeta INSTANCE = new LiveDataBusBeta();
    }

    public static LiveDataBusBeta getInstance() {
        return SingleHolder.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T> MutableLiveData<T> with(String key, Class<T> clazzType) {
        if (!mLiveDataBusBeta.containsKey(key)) {
            mLiveDataBusBeta.put(key, new MutableLiveData<Object>());
        }
        return (MutableLiveData<T>) mLiveDataBusBeta.get(key);
    }

    public MutableLiveData<Object> with(String target) {
        return with(target, Object.class);
    }

    public void remove(String key) {
        mLiveDataBusBeta.remove(key);
    }
}
