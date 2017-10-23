package com.apollo.discounthunter.ui.activity;

import android.os.Message;
import android.view.View;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.widgets.ClockView;

import butterknife.BindView;

/**
 * Created by wangpengbo on 2017/10/23.
 */

public class ShowClockActivity extends BaseActivity {
    @BindView(R.id.clockview)
    ClockView mClockView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_clock;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        mClockView.startTime();
    }

    @Override
    protected void handleMsg(Message msg) {

    }
}
