package com.apollo.discounthunter.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.HomeModel;

import butterknife.BindView;

/**
 * Created by Apollo on 2017/1/16.
 */

public class ShowWebActivity extends BaseActivity {
    @BindView(R.id.webview_showweb)
    WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_showweb;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HomeModel homeModel = bundle.getParcelable(Constants.GOODS_INFO);
            mWebView.loadUrl(homeModel.getWeb_url());
        }
    }
}
