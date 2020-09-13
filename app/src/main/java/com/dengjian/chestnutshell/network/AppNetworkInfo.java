package com.dengjian.chestnutshell.network;

import android.app.Application;
import android.content.Context;

import com.dengjian.chestnutshell.BuildConfig;
import com.dengjian.network.INetworkRequiredInfo;

public class AppNetworkInfo implements INetworkRequiredInfo {
    private final Application mApplication;

    public AppNetworkInfo(Application application) {
        mApplication = application;
    }

    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public Context getApplicationContext() {
        return mApplication;
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
