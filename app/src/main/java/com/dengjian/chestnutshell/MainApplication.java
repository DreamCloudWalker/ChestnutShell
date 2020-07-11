package com.dengjian.chestnutshell;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.dengjian.chestnutshell.network.HttpProcessorProxy;
import com.dengjian.chestnutshell.network.proxy.OkHttpProcessor;
import com.dengjian.chestnutshell.utils.FixDexUtil;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HttpProcessorProxy.init(new OkHttpProcessor());

        // TODO check this
        MultiDex.install(this);
        FixDexUtil.loadFixedDex(this);
    }
}
