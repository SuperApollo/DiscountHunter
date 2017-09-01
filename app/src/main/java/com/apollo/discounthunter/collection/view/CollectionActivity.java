package com.apollo.discounthunter.collection.view;

import android.view.View;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.ui.activity.BaseActivity;

/**
 * Created by Apollo on 2017/8/31 15:59
 */

public class CollectionActivity extends BaseActivity implements ICollectionView {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        setTitle("我的收藏");
    }
}
