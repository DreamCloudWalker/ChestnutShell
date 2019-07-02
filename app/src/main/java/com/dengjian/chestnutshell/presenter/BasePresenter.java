package com.dengjian.chestnutshell.presenter;

import java.lang.ref.WeakReference;

public class BasePresenter<T> {
    public WeakReference<T> mBusinessView;

    public void attachView(T businessView) {
        mBusinessView = new WeakReference<>(businessView);
    }

    public void detachView() {
        if (mBusinessView != null) {
            mBusinessView.clear();
            mBusinessView = null;
        }
    }
}
