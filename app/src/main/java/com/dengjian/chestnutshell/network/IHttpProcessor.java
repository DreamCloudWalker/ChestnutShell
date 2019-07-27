package com.dengjian.chestnutshell.network;

import java.util.Map;

public interface IHttpProcessor {
    void postSync(String url, Map<String, Object> params, ICallbackListener callback);
    void getSync(String url, Map<String, Object> params, ICallbackListener callback);
    void post(String url, Map<String, Object> params, ICallbackListener callback);
    void get(String url, Map<String, Object> params, ICallbackListener callback);
    // TODO add
    // void update();
    // void delete();
}
