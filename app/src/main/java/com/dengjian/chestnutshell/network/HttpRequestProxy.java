package com.dengjian.chestnutshell.network;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestProxy implements IHttpRequest {
    private volatile static HttpRequestProxy mInstance = null;
    private static IHttpRequest mHttp = null;  // contain
    private final Map<String, Object> mParams;
    private String mUrl;

    private HttpRequestProxy() {
        mParams = new HashMap<>();
    }

    public static HttpRequestProxy getInstance() {
        if (null == mInstance) {
            synchronized (HttpRequestProxy.class) {
                if (null == mInstance) {
                    mInstance = new HttpRequestProxy();
                }
            }
        }
        return mInstance;
    }

    public static void init(IHttpRequest http) {
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
    public void get(String url, Map<String, Object> params, ICallbackListener callback) {
        if (null == mHttp) {
            return ;
        }
        mHttp.get(url, params, callback);
    }

    @Override
    public void setUrl(String url) {

    }

    @Override
    public void setData(byte[] data) {

    }

    @Override
    public void execute() {

    }

    @Override
    public void setListener(ICallbackListener listener) {

    }
}
