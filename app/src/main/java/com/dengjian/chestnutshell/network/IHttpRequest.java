package com.dengjian.chestnutshell.network;

import java.util.Map;

public interface IHttpRequest {
    void post(String url, Map<String, Object> params, ICallback callback);
    void get(String url, Map<String, Object> params, ICallback callback);
}
