package com.dengjian.chestnutshell.network.nuthttp;

import com.dengjian.chestnutshell.utils.LogUtil;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
    private static final String TAG = "ThreadManager";
    public static final int CORE_POOL_SIZE = 3;
    public static final int MAX_POOL_SIZE = 10;
    public static final int KEEP_ALIVE_TIME = 15;
    public static final int QUEUE_CAPACITY = 4;
    private static volatile ThreadManager mInstance;
    private final ThreadPoolExecutor mThreadPoolExecutor;
    // 请求队列
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();
    // 重试队列
    private DelayQueue<NutHttpTask> mRetryQueue = new DelayQueue<>();

    public Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable runnable = mQueue.take();
                    mThreadPoolExecutor.execute(runnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public Runnable mRetryRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    NutHttpTask task = mRetryQueue.take();
                    int cnt = task.getCount();
                    if (cnt < 3) {
                        task.setCount(cnt + 1);
                        mThreadPoolExecutor.execute(task);
                        LogUtil.d(TAG, String.format(Locale.ENGLISH, "mRetryRunnable, cnt = %d", cnt));
                    } else {
                        LogUtil.d(TAG, String.format(Locale.ENGLISH, "mRetryRunnable, fail over 3 times"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ThreadManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_CAPACITY), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                addTask(r);
            }
        });
        mThreadPoolExecutor.execute(mRunnable);
    }

    public static ThreadManager getInstance() {
        if (null == mInstance) {
            synchronized (ThreadManager.class) {
                if (null == mInstance) {
                    mInstance = new ThreadManager();
                }
            }
        }
        return mInstance;
    }

    public void addTask(Runnable runnable) {
        if (null == runnable) {
            return ;
        }

        try {
            mQueue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addDelayedTask(NutHttpTask task) {
        if (null == task) {
            return ;
        }
        try {
            task.setDelayTime(3000);
            mRetryQueue.add(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
