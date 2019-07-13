package com.dengjian.chestnutshell.network;

import java.io.InputStream;

public interface ICallbackListener {
    void onSuccess(InputStream result);
    void onFailure(String result);
}
