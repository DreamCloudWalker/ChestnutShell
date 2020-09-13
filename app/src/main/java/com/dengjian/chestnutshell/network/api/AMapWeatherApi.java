package com.dengjian.chestnutshell.network.api;

import com.dengjian.network.error.ErrorHandler;
import com.dengjian.network.NetworkApi;

import io.reactivex.functions.Function;

public class AMapWeatherApi extends NetworkApi {
    public static volatile AMapWeatherApi sInstance;
    public static final String AMAP_KEY = "4effbe0dfa18398078d861df88818455";

    public static AMapWeatherApi getInstance() {
        if (null == sInstance) {
            synchronized (AMapWeatherApi.class) {
                if (null == sInstance) {
                    sInstance = new AMapWeatherApi();
                }
            }
        }

        return sInstance;
    }

    public static <T> T getService(Class<T> service) {
        return getInstance().getRetrofit().create(service);
    }

    @Override
    public String getFormal() {
        return "https://restapi.amap.com/";
    }

    @Override
    public String getTest() {
        return "https://restapi.amap.com/";
    }

    @Override
    public <T> Function<T, T> getAppErrorHandle() {
        return new Function<T, T>() {
            @Override
            public T apply(T t) throws Exception {
                if (t instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) t;
                    if (-1 != response.status) {
                        throw new ErrorHandler.ServerThrowable(response.info, response.status);
                    }
                }
                return t;
            }
        };
    }
}
