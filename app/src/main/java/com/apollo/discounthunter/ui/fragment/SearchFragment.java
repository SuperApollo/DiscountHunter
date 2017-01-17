package com.apollo.discounthunter.ui.fragment;

import com.apollo.discounthunter.R;

/**
 * 搜索界面
 * Created by Apollo on 2017/1/17.
 */

public class SearchFragment extends BaseFragment {
    @Override
    protected void init() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected boolean hideBottom() {
        return true;
    }
}
