package com.apollo.discounthunter.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.Model;

import butterknife.BindView;

/**
 * 展示web页
 * Created by Apollo on 2017/1/16.
 */

public class ShowWebActivity extends BaseActivity {
    @BindView(R.id.webview_showweb)
    WebView mWebView;
    private ActionBar mActionBar;
    private final int START_LOAD = 10086;
    private final int END_LOAD = 10087;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_LOAD:
                    showProgress();
                    break;
                case END_LOAD:
                    clearProgress();
                    break;
            }
        }
    };

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
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mWebView.requestFocusFromTouch();//输入焦点
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//支持js
        //自适应屏幕
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.supportMultipleWindows();  //多窗口
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //页面支持缩放
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {

            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Model homeModel = bundle.getParcelable(Constants.GOODS_INFO);
            mWebView.setWebViewClient(new WebViewClient() {//打开网页时不调用系统浏览器， 而是在本WebView中显示
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        return false;
                    }

                    // Otherwise allow the OS to handle things like tel, mailto, etc.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mHandler.sendEmptyMessage(END_LOAD);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mHandler.sendEmptyMessage(START_LOAD);
                }
            });
            String tag = bundle.getString(Constants.BUNDLE_TAG);
            if (TextUtils.equals("HomeFragment", tag))//显示weburl内容
                mWebView.loadUrl(homeModel.getWeb_url());
            else if (TextUtils.equals("GoodsDetailActivity_quan", tag))//去领券
                mWebView.loadUrl(homeModel.getQuan_link());
            else if (TextUtils.equals("GoodsDetailActivity_buy", tag))//去下单
                mWebView.loadUrl(homeModel.getApp_url());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}