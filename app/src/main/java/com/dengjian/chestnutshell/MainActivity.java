package com.dengjian.chestnutshell;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.dengjian.chestnutshell.activity.BaseActivity;
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
    @InjectView(R.id.tv_main_ui)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryHotFix();
    }

    @NetSubscribe(mode = NetMode.WIFI_CONNECT)
    public void onWifiConnected() {
        Toast.makeText(MainActivity.this, "wifi connected", Toast.LENGTH_SHORT).show();
    }

    @NetSubscribe(mode = NetMode.MOBILE_CONNECT)
    public void onMobileConnected() {
        Toast.makeText(MainActivity.this, "mobile connected", Toast.LENGTH_SHORT).show();
    }

    @NetSubscribe(mode = NetMode.NONE)
    public void onLostNetwork() {
        Toast.makeText(MainActivity.this, "lost network", Toast.LENGTH_SHORT).show();
    }

    @NetSubscribe(mode = NetMode.AUTO)
    public void onNetChange(NetType netType) {
        Toast.makeText(MainActivity.this, "network change", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_main_ui)
    private void clickTextView() {
        Toast.makeText(MainActivity.this, "click test", Toast.LENGTH_SHORT).show();
    }

    @OnLongClick(R.id.tv_main_ui)
    private boolean longClickTextView() {
        Toast.makeText(MainActivity.this, "longclick test", Toast.LENGTH_SHORT).show();
        return false;
    }

    @OnTouch(R.id.tv_main_ui)
    private boolean touchTextView() {
        Toast.makeText(MainActivity.this, "touch test", Toast.LENGTH_SHORT).show();
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
    }
}
