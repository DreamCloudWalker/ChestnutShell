package com.dengjian.chestnutshell.network;

import java.io.InputStream;

public abstract class HttpCallbackListener<Result> implements ICallbackListener {
    @Override
    public void onSuccess(InputStream result) {
        // TODO
        onSuccess((Result) result);
    }

    public abstract void onSuccess(Result result);

    @Override
    public void onFailure(String result) {

    }
}
