package com.dengjian.chestnutshell.network;

import java.util.Map;

public interface IHttpRequest {
    void setListener(ICallbackListener listener);
    void setUrl(String url);
    void execute();
    void setData(byte[] data);
    void post(String url, Map<String, Object> params, ICallbackListener callback);
    void get(String url, Map<String, Object> params, ICallbackListener callback);
}
