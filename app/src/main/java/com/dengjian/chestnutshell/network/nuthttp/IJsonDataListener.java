package com.dengjian.chestnutshell.network.nuthttp;

public interface IJsonDataListener<T> {
    void onSuccess(T result);
    void onFailure(String result);
}
