package com.dengjian.chestnutshell.network.nuthttp;

import com.dengjian.chestnutshell.network.ICallbackListener;
import com.dengjian.chestnutshell.network.IHttpRequest;

public class NutHttp {

    public static<T, V> void sendRequest(String url, T requestData, Class<V> response, IJsonDataListener listener) {
        // 创建请求对象
        IHttpRequest iHttpRequest = new JsonHttpRequest();
        // 创建回调接口
        ICallbackListener callBackListener = new JsonCallbackListener(response, listener);
        // 二次封装请求对象，将请求对象变成一个线程对象
        NutHttpTask httpTask = new NutHttpTask(url, requestData, callBackListener, iHttpRequest);
        // 请求对象入队
        ThreadManager.getInstance().addTask(httpTask);
    }
}
