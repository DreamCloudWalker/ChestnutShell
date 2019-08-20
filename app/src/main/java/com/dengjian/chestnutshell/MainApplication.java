package com.dengjian.chestnutshell;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.dengjian.chestnutshell.network.HttpProcessorProxy;
import com.dengjian.chestnutshell.network.proxy.OkHttpProcessor;
import com.dengjian.chestnutshell.utils.FixDexUtil;
import com.squareup.leakcanary.LeakCanary;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        HttpProcessorProxy.init(new OkHttpProcessor());

        // TODO check this
        MultiDex.install(this);
        FixDexUtil.loadFixedDex(this);
    }
}
