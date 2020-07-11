package com.dengjian.chestnutshell.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dengjian.chestnutshell.ioc.InjectManager;
import com.dengjian.chestnutshell.presenter.BasePresenter;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
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
