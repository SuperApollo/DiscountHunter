package com.apollo.discounthunter.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.utils.IntentUtils;

/**
 * Created by Apollo on 2017/8/31 17:01
 */

public class WelcomeActivity extends Activity {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            IntentUtils.sendIntent(WelcomeActivity.this, MainActivity.class);
            WelcomeActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
//        getActionBar().hide();
        mHandler.sendEmptyMessageDelayed(0, 3000);
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
