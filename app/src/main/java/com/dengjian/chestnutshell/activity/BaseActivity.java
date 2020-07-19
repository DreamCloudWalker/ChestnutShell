package com.dengjian.chestnutshell.activity;

import android.os.Bundle;

import com.dengjian.chestnutshell.ioc.InjectManager;
import com.dengjian.chestnutshell.presenter.BasePresenter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    public T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // IOC依赖注入
        InjectManager.inject(this);

        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        InjectManager.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        InjectManager.unregister(this);
    }

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
