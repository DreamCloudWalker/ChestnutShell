package com.dengjian.chestnutshell.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dengjian.annotations.BindPath;
import com.dengjian.chestnutshell.R;
import com.dengjian.chestnutshell.databus.LiveDataBus;
import com.dengjian.chestnutshell.highlevelui.koifish.KoiFishDrawable;
import com.dengjian.chestnutshell.highlevelui.particleexplode.ExplosionView;
import com.dengjian.chestnutshell.highlevelui.particleexplode.FallingParticleFactory;
import com.dengjian.chestnutshell.ioc.annotation.ContentView;
import com.dengjian.chestnutshell.ioc.annotation.InjectView;
import com.dengjian.chestnutshell.ioc.annotation.NetSubscribe;
import com.dengjian.chestnutshell.ioc.annotation.OnClick;
import com.dengjian.chestnutshell.ioc.annotation.OnLongClick;
import com.dengjian.chestnutshell.ioc.annotation.OnTouch;
import com.dengjian.chestnutshell.ioc.typs.NetMode;
import com.dengjian.chestnutshell.ioc.typs.NetType;
import com.dengjian.chestnutshell.model.BusinessModel;
import com.dengjian.chestnutshell.network.api.AMapWeatherApi;
import com.dengjian.chestnutshell.network.api.Weather;
import com.dengjian.chestnutshell.network.api.WeatherInterface;
import com.dengjian.network.error.ErrorHandler;
import com.dengjian.network.observer.BaseObserver;
import com.dengjian.chestnutshell.presenter.BusinessPresenter;
import com.dengjian.chestnutshell.utils.FileUtil;
import com.dengjian.chestnutshell.utils.LogUtil;
import com.dengjian.chestnutshell.view.IBusinessView;
import com.dengjian.nutpermission.annotations.PermissionDenied;
import com.dengjian.nutpermission.annotations.PermissionNeed;
import com.dengjian.nutrouter.NutRouter;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import static com.dengjian.chestnutshell.network.api.AMapWeatherApi.AMAP_KEY;

@BindPath("main/main")
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity<IBusinessView, BusinessPresenter<IBusinessView>>
        implements IBusinessView {
    private static final String TAG = "MainActivity";
    public static final String KEY_MAIN_ACTIVITY_LIVE_DATA = "KEY_MAIN_ACTIVITY_LIVE_DATA";
    private static final int CODE_CHECK_STORAGE_PERMISSION = 1;
    private static final boolean ENABLE_HOT_FIX = true;

    @InjectView(R.id.tv_next_page)
    private TextView mTvNextPage;

    @InjectView(R.id.tv_send_data)
    private TextView mTvSendData;

    @InjectView(R.id.btn_login)
    private Button mBtnLogin;

    @InjectView(R.id.btn_weather)
    private Button mBtnWeather;

    @InjectView(R.id.tv_console)
    private TextView mTvConsole;

    @InjectView(R.id.iv_explosion_view)
    private ImageView mIvExplosionView;

    private int mClickCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ENABLE_HOT_FIX)
            queryHotFix();

        ExplosionView explosionView = new ExplosionView(this);
        explosionView.setParticleFactory(new FallingParticleFactory());
        explosionView.addListener(mIvExplosionView);
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

    @OnClick(R.id.btn_login)
    private void clickLogin() {
        NutRouter.getInstance().startActivity("nutlogin/login", null);
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_weather)
    private void clickGetWeather() {
        AMapWeatherApi.getService(WeatherInterface.class)
                .getWeather("110101", AMAP_KEY)
                .compose(AMapWeatherApi.getInstance().subscribe(new BaseObserver<Weather>() {
                    // compose 合并一系列操作
                    @Override
                    public void onSuccess(Weather weather) {
                        mTvConsole.setText(weather.toString());
                    }

                    @Override
                    public void onFailure(ErrorHandler.ResponseThrowable e) {
                        mTvConsole.setText(e.toString());
                        e.printStackTrace();
                    }
                }));
    }

    @OnClick(R.id.tv_send_data)
    private void clickSendData() {
        mClickCnt++;
        LiveDataBus.getInstance().with(KEY_MAIN_ACTIVITY_LIVE_DATA, String.class).setValue(
                "Send data in the same page, click count = " + mClickCnt);

        loadPicData();
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

    @PermissionNeed(permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
            requestCode = CODE_CHECK_STORAGE_PERMISSION)
    private void loadPicData() {
        new Thread() {
            @Override
            public void run() {
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] projections = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA,
                                        MediaStore.Images.ImageColumns.DATE_MODIFIED};
                String orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC";
                Cursor cursor = getContentResolver().query(uri, projections, null, null, orderBy);
                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(0);
                        String path = cursor.getString(1);
                        long dateModified = cursor.getInt(2);
                        Log.d(TAG, String.format("Image found: imageId = %d, imagePath = %s, dateModified = %d",
                                id, path, dateModified));
                        if (id <= 0 || null == path || path.isEmpty()) { // TODO check invalid media ID
                            continue ;
                        }
                        // fill list
                    }
                    cursor.close();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }.start();
    }

    @PermissionDenied
    private void permissionDenied(int requestCode) {
        switch (requestCode) {
            case CODE_CHECK_STORAGE_PERMISSION:
                Log.d(TAG, "存储权限被拒绝！");
                Toast.makeText(this, "存储权限被拒绝!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
