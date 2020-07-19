package com.dengjian.chestnutshell.databus;

import android.os.Looper;

import com.dengjian.chestnutshell.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class LiveDataBus {
    private static final String TAG = "LiveDataBus";
    private static volatile LiveDataBus mInstance = null;
    private static Map<String, CustomMutableLiveData> mLiveDatas = new ConcurrentHashMap<>();

    private LiveDataBus() {

    }

    public static LiveDataBus getInstance() {
        if (null == mInstance) {
            synchronized (LiveDataBus.class) {
                if (null == mInstance) {
                    mInstance = new LiveDataBus();
                }
            }
        }

        return mInstance;
    }

    public <T> CustomMutableLiveData<T> with(String key, Class<T> clazz) {
        if (!mLiveDatas.containsKey(key)) {
            mLiveDatas.put(key, new CustomMutableLiveData());
        }

        return mLiveDatas.get(key);
    }

    public CustomMutableLiveData<Object> with(String key) {
        return with(key, Object.class);
    }

    public void remove(String key) {
        mLiveDatas.remove(key);
    }

    public <T> void post(String key, T t) {
        if (Looper.getMainLooper() == Looper.myLooper()) {  // in main thread
            with(key).setValue(t);
        } else {
            with(key).postValue(t);
        }
    }

    public static class CustomMutableLiveData<T> extends MutableLiveData<T> {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) { // TODO check super
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // stick event
        public void observeSticky(@NotNull LifecycleOwner owner, @NotNull Observer<T> observer) {
            super.observe(owner, observer);
        }

        private void hook(@NotNull Observer<? super T> observer) throws Exception {
            LogUtil.d(TAG, "Start of Hook");
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");    // 反射LiveData源码的mObservers
            fieldObservers.setAccessible(true);
            Object observerObj = fieldObservers.get(this);

            Class<?> classObservers = observerObj.getClass(); // SafeIterableMap
            Method method = classObservers.getDeclaredMethod("get", Object.class);
            method.setAccessible(true);
            Object entry = method.invoke(observerObj, observer);    // SafeIterableMap$Entry
            Object observerWrapper = ((Map.Entry) entry).getValue();    // LiveData$LifecycleBoundObserver
            Class<?> observerClazz = observerWrapper.getClass().getSuperclass();  // ObserverWrapper
            Field lastVersion = observerClazz.getDeclaredField("mLastVersion");
            lastVersion.setAccessible(true);

            Field version = classLiveData.getDeclaredField("mVersion"); // 反射LiveData源码的mVersion
            version.setAccessible(true);
            Object versionValue = version.get(this);

            lastVersion.set(observerWrapper, versionValue);
            LogUtil.d(TAG, "End of Hook");
        }
    }
}
