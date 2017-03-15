package com.apollo.discounthunter.ui.activity;

import android.view.View;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.widgets.ItemView;

import butterknife.BindView;

/**
 * Created by apollo on 17-3-14.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.item_setting_about)
    ItemView itemAbout;
    @BindView(R.id.item_setting_flash)
    ItemView itemFlash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        setTitle("我的设置");
        itemAbout.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                IntentUtils.sendIntent(SettingActivity.this, AboutActivity.class);
            }
        });
        itemFlash.setToggleButtonChangeListner(new ItemView.OnToggleButtonChangeListner() {
            @Override
            public void onToggleChanged(boolean on) {
                mToastUtils.show(SettingActivity.this, on + "");
            }
        });
    }
}
