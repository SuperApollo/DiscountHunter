package com.apollo.discounthunter.ui.fragment;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.widgets.XListView;

import butterknife.BindView;

/**
 * Created by Apollo on 2017/1/13.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.xlv_home)
    XListView mXlvHome;

    @Override
    protected void init() {



    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected boolean hideBottom() {
        return false;
    }
}
