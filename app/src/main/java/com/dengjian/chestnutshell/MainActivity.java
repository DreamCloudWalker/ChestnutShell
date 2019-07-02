package com.dengjian.chestnutshell;

import android.os.Bundle;

import com.dengjian.chestnutshell.activity.BaseActivity;
import com.dengjian.chestnutshell.model.BusinessModel;
import com.dengjian.chestnutshell.presenter.BusinessPresenter;
import com.dengjian.chestnutshell.view.IBusinessView;

import java.util.List;

public class MainActivity extends BaseActivity<IBusinessView, BusinessPresenter<IBusinessView>> implements IBusinessView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
