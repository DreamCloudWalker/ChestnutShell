package com.dengjian.network;

import android.annotation.SuppressLint;

import com.dengjian.network.dns.AlibabaDns;
import com.dengjian.network.error.HttpError;
import com.dengjian.network.interceptor.CommonRequestInterceptor;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NetworkApi {
    private static final String TAG = "NetworkApi";
    private static INetworkRequiredInfo mNetworkRequiredInfo;
    private static OkHttpClient mOkHttpClient;
    private static Map<String, Retrofit> mRetrofitMap = new HashMap<>();
    private static boolean mIsFormal;
    private String mBaseUrl;

    public NetworkApi() {
        if (mIsFormal) {
            mBaseUrl = getFormal();
        } else {
            mBaseUrl = getTest();
        }
    }

    public static void init(INetworkRequiredInfo iNetworkRequiredInfo) {
        mNetworkRequiredInfo = iNetworkRequiredInfo;
        mIsFormal = false;    // EnvironmentActivity.isOfficialEnvironment(iNetworkRequiredInfo.getApplicationContext());
    }

    public Retrofit getRetrofit() {
        Retrofit retrofit = mRetrofitMap.get(mBaseUrl);
        if (null != retrofit) {
            return retrofit;
        }

        retrofit = new Retrofit.Builder().baseUrl(mBaseUrl)
                                         .callFactory(getOkHttpClient())
                                         .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                         .addConverterFactory(GsonConverterFactory.create()).build();
        mRetrofitMap.put(mBaseUrl, retrofit);
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        if (null != mOkHttpClient) {
            return mOkHttpClient;
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new CommonRequestInterceptor(mNetworkRequiredInfo));

        if (null != mNetworkRequiredInfo && mNetworkRequiredInfo.isDebug()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(loggingInterceptor);
        }

        // for android <7.0, >7.0 will use network_security_config.xml
        builder.certificatePinner(new CertificatePinner.Builder()
                .add("restapi.amap.com", "sha256//6WD2EQVJUtFa3zd+7JlFZtf5NYejXZI/qmPKSZsY5I=")
                .add("restapi.amap.com", "sha256/IQBnNBEiFuhj+8x6X8XLgh01V9Ic5/V3IRQLNFFc7v4=").build());

        if (null != mNetworkRequiredInfo) {
//            builder.dns(new AlibabaDns(mNetworkRequiredInfo.getApplicationContext()));
        }

        mOkHttpClient = builder.build();
        return mOkHttpClient;
    }

    public <T> ObservableTransformer<T, T> subscribe(final Observer<T> observer) {
        return new ObservableTransformer<T, T>() {
            @SuppressLint("CheckResult")
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable =  upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                if (null != getAppErrorHandle()) {
                    // RxJava的map操作符是把源Observable产生的结果通过映射规则转换成另一个结果集，并提交给订阅者处理
                    observable = observable.map(NetworkApi.this.<T>getAppErrorHandle());
                }

                observable = observable.onErrorResumeNext(new HttpError<T>());
                observable.subscribe(observer);
                return observable;
            }
        };
    }

    public abstract String getFormal();
    public abstract String getTest();

    public abstract <T> Function<T, T> getAppErrorHandle();
}
