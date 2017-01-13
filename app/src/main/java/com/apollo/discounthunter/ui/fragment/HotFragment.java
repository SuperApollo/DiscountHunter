package com.apollo.discounthunter.ui.fragment;

import com.apollo.discounthunter.R;

/**
 * Created by Apollo on 2017/1/13.
 */

public class HotFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected boolean hideBottom() {
        return false;
    }
}
