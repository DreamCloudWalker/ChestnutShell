package com.dengjian.chestnutshell.network.proxy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.dengjian.chestnutshell.network.ICallbackListener;
import com.dengjian.chestnutshell.network.IHttpRequest;

import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRequestModel implements IHttpRequest {
    private static final String TAG = "OkHttpRequestModel";

    private final OkHttpClient mOkHttpClient;
    private final Handler mHandler;

    public OkHttpRequestModel(Context context) {
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallbackListener callback) {

    }

    @Override
    public void get(String url, Map<String, Object> params, ICallbackListener callback) {
        if (TextUtils.isEmpty(url)) {
            return ;
        }
        try {
            final Request request = new Request.Builder().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();

            if (null != callback) {
                callback.onSuccess(execute.body().byteStream());    // TODO to check
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setData(byte[] data) {

    }

    @Override
    public void setUrl(String url) {

    }

    @Override
    public void setListener(ICallbackListener listener) {

    }

    @Override
    public void execute() {

    }
}
