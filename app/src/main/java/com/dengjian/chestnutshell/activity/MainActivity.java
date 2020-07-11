package com.dengjian.chestnutshell.activity;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.dengjian.chestnutshell.R;
import com.dengjian.chestnutshell.databus.LiveDataBus;
import com.dengjian.chestnutshell.ioc.annotation.ContentView;
import com.dengjian.chestnutshell.ioc.annotation.InjectView;
import com.dengjian.chestnutshell.ioc.annotation.NetSubscribe;
import com.dengjian.chestnutshell.ioc.annotation.OnClick;
import com.dengjian.chestnutshell.ioc.annotation.OnLongClick;
import com.dengjian.chestnutshell.ioc.annotation.OnTouch;
import com.dengjian.chestnutshell.ioc.typs.NetMode;
import com.dengjian.chestnutshell.ioc.typs.NetType;
import com.dengjian.chestnutshell.model.BusinessModel;
import com.dengjian.chestnutshell.presenter.BusinessPresenter;
import com.dengjian.chestnutshell.utils.FileUtil;
import com.dengjian.chestnutshell.utils.LogUtil;
import com.dengjian.chestnutshell.view.IBusinessView;

import java.io.File;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity<IBusinessView, BusinessPresenter<IBusinessView>>
        implements IBusinessView {
    private static final String TAG = "MainActivity";
    public static final String KEY_MAIN_ACTIVITY_LIVE_DATA = "KEY_MAIN_ACTIVITY_LIVE_DATA";

    @InjectView(R.id.tv_next_page)
    private TextView mTvNextPage;

    @InjectView(R.id.tv_send_data)
    private TextView mTvSendData;

    private int mClickCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryHotFix();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LiveDataBus.getInstance().with(KEY_MAIN_ACTIVITY_LIVE_DATA, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String str) {
                LogUtil.d(TAG, "LiveDataBus, OnChanged: str = " + str);
                mTvSendData.setText(str);
            }
        });
    }

    @NetSubscribe(mode = NetMode.WIFI_CONNECT)
    public void onWifiConnected() {
        LogUtil.d(TAG, "wifi connected");
    }

    @NetSubscribe(mode = NetMode.MOBILE_CONNECT)
    public void onMobileConnected() {
        LogUtil.d(TAG, "mobile connected");
    }

    @NetSubscribe(mode = NetMode.NONE)
    public void onLostNetwork() {
        LogUtil.d(TAG, "lost connected");
    }

    @NetSubscribe(mode = NetMode.AUTO)
    public void onNetChange(NetType netType) {
        LogUtil.d(TAG, "network change");
    }

    @OnClick(R.id.tv_next_page)
    private void clickNextPage() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_send_data)
    private void clickSendData() {
        mClickCnt++;
        LiveDataBus.getInstance().with(KEY_MAIN_ACTIVITY_LIVE_DATA, String.class).setValue(
                "Send data in the same page, click count = " + mClickCnt);
    }

    @OnLongClick(R.id.tv_next_page)
    private boolean longClickTextView() {
        LogUtil.d(TAG, "longclick test");
        return false;
    }

    @OnTouch(R.id.tv_next_page)
    private boolean touchTextView() {
        LogUtil.d(TAG, "touch test");
        return false;
    }

    private void queryHotFix() {
        ClassLoader classLoader = MainActivity.class.getClassLoader();
        while (null != classLoader) {
            LogUtil.d(TAG, String.format("queryHotFix, classLoader = %s\n", classLoader.toString()));
            classLoader = classLoader.getParent();
        }

        // TODO
        File sourceFile = new File(Environment.getExternalStorageDirectory(), "classes2.dex");
        // /data/user/o/packagename/app_odex/classes2.dex
        File targetFile = new File(getDir("odex", Context.MODE_PRIVATE).getAbsolutePath()
                + File.separator + "classes2.dex");
        if (targetFile.exists()) {
            targetFile.delete();
        }
        if (FileUtil.copyFile(sourceFile, targetFile)) {
            LogUtil.d(TAG, "queryHotFix, copy success");
        } else {
            LogUtil.e(TAG, "queryHotFix, copy failed");

        }
    }

    @Override
    protected BusinessPresenter<IBusinessView> createPresenter() {
        return new BusinessPresenter<>();
    }

    @Override
    public void showBusinessView(List<BusinessModel> datas) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataBus.getInstance().remove(KEY_MAIN_ACTIVITY_LIVE_DATA);
    }
}
