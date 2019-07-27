package com.dengjian.chestnutshell.network.nuthttp;

import com.dengjian.chestnutshell.network.ICallbackListener;

public interface IHttpRequest {
    void setListener(ICallbackListener listener);
    void setUrl(String url);
    void execute();
    void setData(byte[] data);
}
