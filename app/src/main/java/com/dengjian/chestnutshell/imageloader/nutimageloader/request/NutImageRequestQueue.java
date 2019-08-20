package com.dengjian.chestnutshell.imageloader.nutimageloader.request;

import com.dengjian.chestnutshell.utils.LogUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求队列, 使用优先队列,使得请求可以按照优先级进行处理. [ Thread Safe ]
 */
public final class NutImageRequestQueue {
    /**
     * 请求队列 [ Thread-safe ]
     */
    private BlockingQueue<NutImageRequest> mRequestQueue = new PriorityBlockingQueue<NutImageRequest>();
    /**
     * 请求的序列化生成器
     */
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);

    /**
     * 默认的核心数
     */
    public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;
    /**
     * CPU核心数 + 1个分发线程数
     */
    private int mDispatcherNums = DEFAULT_CORE_NUMS;
    /**
     * NetworkExecutor,执行网络请求的线程
     */
    private NutRequestDispatcher[] mDispatchers = null;

    /**
     *
     */
    protected NutImageRequestQueue() {
        this(DEFAULT_CORE_NUMS);
    }

    /**
     * @param coreNums 线程核心数
     */
    public NutImageRequestQueue(int coreNums) {
        mDispatcherNums = coreNums;
    }

    /**
     * 启动RequestDispatcher
     */
    private final void startDispatchers() {
        mDispatchers = new NutRequestDispatcher[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            LogUtil.i("", "start thread: " + i);
            mDispatchers[i] = new NutRequestDispatcher(mRequestQueue);
            mDispatchers[i].start();
        }
    }

    public void start() {
        stop();
        startDispatchers();
    }

    /**
     * 停止RequestDispatcher
     */
    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                mDispatchers[i].interrupt();
            }
        }
    }

    /**
     * 不能重复添加请求
     *
     * @param request
     */
    public void addRequest(NutImageRequest request) {
        if (!mRequestQueue.contains(request)) {
            request.mSerialNum = this.generateSerialNumber();
            mRequestQueue.add(request);
        } else {
            LogUtil.d("", "### 请求队列中已经含有");
        }
    }

    public void clear() {
        mRequestQueue.clear();
    }

    public BlockingQueue<NutImageRequest> getAllRequests() {
        return mRequestQueue;
    }

    /**
     * 为每个请求生成一个系列号
     *
     * @return 序列号
     */
    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }
}
