package com.dengjian.chestnutshell.network;

import com.google.gson.Gson;

public abstract class HttpCallbackListener<Result> implements ICallbackListener {
    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> clazz = HttpProxyUtil.analysisClassInfo(this);
        Result objResult = (Result) gson.fromJson(result, clazz);

        // 给实现类
        onSuccess(objResult);
    }

    @Override
    public void onFailure(String result) {

    }

    public abstract void onSuccess(Result result);
}
