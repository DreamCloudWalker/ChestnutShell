package com.dengjian.network.interceptor;

import com.dengjian.network.INetworkRequiredInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonRequestInterceptor implements Interceptor {
    private final INetworkRequiredInfo mNetworkRequiredInfo;

    public CommonRequestInterceptor(INetworkRequiredInfo networkRequiredInfo) {
        mNetworkRequiredInfo = networkRequiredInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("os", "android");
        builder.addHeader("version", mNetworkRequiredInfo.getAppVersionName());
        Request newRequest = builder.build();

        return chain.proceed(newRequest);
    }
}
