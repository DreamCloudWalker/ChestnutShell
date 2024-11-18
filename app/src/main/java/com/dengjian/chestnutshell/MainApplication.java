package com.dengjian.chestnutshell;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.dengjian.chestnutshell.network.AppNetworkInfo;
import com.dengjian.chestnutshell.network.HttpProcessorProxy;
import com.dengjian.chestnutshell.network.proxy.OkHttpProcessor;
import com.dengjian.chestnutshell.utils.FixDexUtil;
import com.dengjian.common.utils.StorageUtil;
import com.dengjian.network.NetworkApi;
import com.dengjian.nutrouter.NutRouter;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StorageUtil.init(this);
        NetworkApi.init(new AppNetworkInfo(this));
        NutRouter.getInstance().init(this);

        HttpProcessorProxy.init(new OkHttpProcessor());

        // TODO check this
        MultiDex.install(this);
        FixDexUtil.loadFixedDex(this);
    }
}
