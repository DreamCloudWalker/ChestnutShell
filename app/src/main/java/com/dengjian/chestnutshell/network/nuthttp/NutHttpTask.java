package com.dengjian.chestnutshell.network.nuthttp;

import com.dengjian.chestnutshell.network.ICallbackListener;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class NutHttpTask<T> implements Runnable, Delayed {
    private IHttpRequest mIHttpRequest;
    private int mCount;
    private long mDelayTime;

    public NutHttpTask(String url, T requestData, ICallbackListener callbackListener, IHttpRequest iHttpRequest) {
        mIHttpRequest = iHttpRequest;
        mIHttpRequest.setUrl(url);
        mIHttpRequest.setListener(callbackListener);
        if (null != requestData) {
            Gson gson = new Gson();
            String dataStr = gson.toJson(requestData);
            try {
                mIHttpRequest.setData(dataStr.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            mIHttpRequest.execute();
        } catch (Exception e) {
            ThreadManager.getInstance().addDelayedTask(this);
        }
    }

    public void setCount(int count) {
        mCount = count;
    }

    public int getCount() {
        return mCount;
    }

    public long getDelayTime() {
        return mDelayTime;
    }

    public void setDelayTime(int delayTime) {
        mDelayTime = delayTime + System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getDelayTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
