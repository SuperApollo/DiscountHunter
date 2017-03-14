package com.apollo.discounthunter.ui.activity;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.widgets.ItemView;

import butterknife.BindView;

/**
 * Created by apollo on 17-3-14.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.item_about_update)
    ItemView itemUpdate;
    @BindView(R.id.item_about_suggestion)
    ItemView itemSuggestion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView() {
        itemUpdate.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                mToastUtils.show(AboutActivity.this, "检查更新");
            }
        });

        itemSuggestion.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                mToastUtils.show(AboutActivity.this, "意见反馈");
            }
        });
    }

}
