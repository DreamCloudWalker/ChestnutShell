package com.dengjian.chestnutshell.network;

import java.util.Map;

/**
 * 给开发使用的代理类
 */
public class HttpProcessorProxy implements IHttpProcessor {
    private volatile static HttpProcessorProxy mInstance = null;
    private static IHttpProcessor mHttp = null;  // contain

    private HttpProcessorProxy() {

    }

    public static HttpProcessorProxy getInstance() {
        if (null == mInstance) {
            synchronized (HttpProcessorProxy.class) {
                if (null == mInstance) {
                    mInstance = new HttpProcessorProxy();
                }
            }
        }
        return mInstance;
    }

    // 初始化注入
    public static void init(IHttpProcessor http) {
        mHttp = http;
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallbackListener callback) {
        if (null == mHttp) {
            return ;
        }
        mHttp.post(url, params, callback);
    }

    @Override
    public void postSync(String url, Map<String, Object> params, ICallbackListener callback) {
        if (null == mHttp) {
            return ;
        }
        mHttp.postSync(url, params, callback);
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallbackListener callback) {
        if (null == mHttp) {
            return ;
        }
        mHttp.get(url, params, callback);
    }

    @Override
    public void getSync(String url, Map<String, Object> params, ICallbackListener callback) {
        if (null == mHttp) {
            return ;
        }
        mHttp.getSync(url, params, callback);
    }
}
