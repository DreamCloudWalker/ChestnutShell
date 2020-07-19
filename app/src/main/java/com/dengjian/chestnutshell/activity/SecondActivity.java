package com.dengjian.chestnutshell.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.dengjian.chestnutshell.R;
import com.dengjian.chestnutshell.databus.LiveDataBus;
import com.dengjian.chestnutshell.databus.LiveDataBusBeta;
import com.dengjian.chestnutshell.ioc.annotation.ContentView;
import com.dengjian.chestnutshell.ioc.annotation.InjectView;
import com.dengjian.chestnutshell.ioc.annotation.OnClick;
import com.dengjian.chestnutshell.model.BusinessModel;
import com.dengjian.chestnutshell.presenter.BusinessPresenter;
import com.dengjian.chestnutshell.utils.LogUtil;
import com.dengjian.chestnutshell.view.IBusinessView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import static com.dengjian.chestnutshell.activity.MainActivity.KEY_MAIN_ACTIVITY_LIVE_DATA;

@ContentView(R.layout.activity_second)
public class SecondActivity extends BaseActivity<IBusinessView, BusinessPresenter<IBusinessView>>
        implements IBusinessView {
    private static final String TAG = "SecondActivity";

    @InjectView(R.id.tv_send_data_from_other_page)
    private TextView mTvSendData;

    private int mClickCnt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LiveDataBus.getInstance().with(KEY_MAIN_ACTIVITY_LIVE_DATA, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String str) {
                LogUtil.d(TAG, "LiveDataBus, OnChanged: str = " + str);
                mTvSendData.setText(str);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataBus.getInstance().remove(KEY_MAIN_ACTIVITY_LIVE_DATA);
    }

    @OnClick(R.id.tv_send_data_from_other_page)
    private void clickSendData() {
        mClickCnt++;
        LiveDataBus.getInstance().with(KEY_MAIN_ACTIVITY_LIVE_DATA, String.class).setValue(
                "Send data from other page, click count = " + mClickCnt);
    }

    @Override
    protected BusinessPresenter<IBusinessView> createPresenter() {
        return new BusinessPresenter<>();
    }

    @Override
    public void showBusinessView(List<BusinessModel> datas) {

    }
}
