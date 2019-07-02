package com.dengjian.chestnutshell.presenter;

import com.dengjian.chestnutshell.view.IBusinessView;

public class BusinessPresenter<T extends IBusinessView> extends BasePresenter<T> {

    @Override
    public void attachView(T businessView) {
        super.attachView(businessView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void fetch() {

    }
}
