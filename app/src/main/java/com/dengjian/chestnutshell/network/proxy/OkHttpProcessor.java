package com.dengjian.chestnutshell.network.proxy;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.dengjian.chestnutshell.network.ICallbackListener;
import com.dengjian.chestnutshell.network.IHttpProcessor;
import com.dengjian.chestnutshell.utils.LogUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpProcessor implements IHttpProcessor {
    private static final String TAG = "OkHttpProcessor";

    private final OkHttpClient mOkHttpClient;
    private final Handler mHandler;

    public OkHttpProcessor() {
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void get(String url, Map<String, Object> params, final ICallbackListener callback) {
        if (TextUtils.isEmpty(url)) {
            return ;
        }
        try {
            final Request request = new Request.Builder().get().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                LogUtil.e(TAG, "get, onFailure e = ", e);
                                callback.onFailure(e.toString());
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response == null) {
                        LogUtil.d(TAG, "get, onSuccess response== null");
                        return;
                    }
                    if (response.isSuccessful()) {
                        final String result = response.body().string();
                        LogUtil.d(TAG, "get, onSuccess result = " + result);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (null != callback) {
                                    callback.onSuccess(result);
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (null != callback) {
                                    callback.onFailure(response.message());
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSync(String url, Map<String, Object> params, ICallbackListener callback) {
        if (TextUtils.isEmpty(url)) {
            return ;
        }
        try {
            final Request request = new Request.Builder().get().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            Response response = call.execute();

            if (null != callback) {
                if (null != response && response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(response.message());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void post(String url, Map<String, Object> params, final ICallbackListener callback) {
        if (TextUtils.isEmpty(url)) {
            return ;
        }
        try {
            RequestBody requestbody = appendBody(params);
            final Request request = new Request.Builder()
                    .post(requestbody)
                    .url(url)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    LogUtil.e(TAG, "onFailure e =", e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onFailure(e.toString());
                            }
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response == null) {
                        LogUtil.d(TAG, "onSuccess response == null");
                        return;
                    }
                    LogUtil.d(TAG, "onSuccess response = " + response.toString());
                    if (response.isSuccessful()) {
                        final String result = response.body().string();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (null != callback) {
                                    callback.onSuccess(result);
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (null != callback) {
                                    callback.onFailure(response.message());
                                }
                            }
                        });

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postSync(String url, Map<String, Object> params, ICallbackListener callback) {
        if (TextUtils.isEmpty(url)) {
            return ;
        }
        try {
            RequestBody requestbody = appendBody(params);
            final Request request = new Request.Builder()
                    .post(requestbody)
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            Response response = call.execute();

            if (null != callback) {
                if (null != response && response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(response.message());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestBody appendBody(Map<String, Object> params) {
        FormBody.Builder body = new FormBody.Builder();
        if (params == null || params.isEmpty()) {
            return body.build();
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            body.add(entry.getKey(), entry.getValue().toString());
        }
        return body.build();
    }
}
