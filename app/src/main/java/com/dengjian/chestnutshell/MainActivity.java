package com.dengjian.chestnutshell;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.dengjian.chestnutshell.activity.BaseActivity;
import com.dengjian.chestnutshell.model.BusinessModel;
import com.dengjian.chestnutshell.presenter.BusinessPresenter;
import com.dengjian.chestnutshell.utils.FileUtil;
import com.dengjian.chestnutshell.utils.LogUtil;
import com.dengjian.chestnutshell.view.IBusinessView;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity<IBusinessView, BusinessPresenter<IBusinessView>>
        implements IBusinessView {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queryHotFix();

        setContentView(R.layout.activity_main);
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
