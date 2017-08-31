package com.apollo.discounthunter.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.utils.IntentUtils;

/**
 * Created by Apollo on 2017/8/31 17:01
 */

public class WelcomeActivity extends BaseActivity {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            IntentUtils.sendIntent(mContext, MainActivity.class);
            WelcomeActivity.this.finish();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        mHandler.sendEmptyMessageDelayed(0,3000);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
