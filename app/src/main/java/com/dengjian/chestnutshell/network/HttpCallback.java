package com.dengjian.chestnutshell.network;

import com.dengjian.chestnutshell.network.ICallback;

public abstract class HttpCallback<Result> implements ICallback {
    @Override
    public void onSuccess(String result) {
        // TODO
        onSuccess((Result) result);
    }

    public abstract void onSuccess(Result result);

    @Override
    public void onFailure(String result) {

    }
}
