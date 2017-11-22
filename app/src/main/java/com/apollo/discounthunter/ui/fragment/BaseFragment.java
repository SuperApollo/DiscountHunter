package com.apollo.discounthunter.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.base.BaseApplication;
import com.apollo.discounthunter.utils.ToastUtils;
import com.apollo.discounthunter.widgets.CustomProgressView;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Apollo on 2017/1/13.
 */

public abstract class BaseFragment extends Fragment {
    protected CustomProgressView customProgressView;
    protected Context mContext;
    RadioGroup mRbBottom;
    protected ToastUtils mToastUtils;
    protected String TAG;
    protected boolean isVisible;//当前fragment是否可见
    protected boolean isPrepared;//当前fragment视图是否准备好
    //统一提取 handler 到基类，处理内存泄漏
    protected Myhandler mHandler = new Myhandler() {
        @Override
        public void handleMessage(Message msg) {
            handleMsg(msg);
        }
    };
    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getContext();
        mToastUtils = ToastUtils.shareInstance();
        TAG = getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        isPrepared = true;
        mUnbinder = ButterKnife.bind(this, view);
        lazyLoad();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());//统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void initView() {
        Activity parent = getActivity();
        if (parent != null) {
            mRbBottom = (RadioGroup) parent.findViewById(R.id.rg_main_bottom);
            if (hideBottom())
                mRbBottom.setVisibility(View.GONE);
            else
                mRbBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRbBottom != null)
            mRbBottom.setVisibility(View.VISIBLE);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mUnbinder.unbind();
    }

    /**
     * 当前fragment可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 懒加载
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initView();
        init();
    }

    /**
     * 当前fragment不可见
     */
    protected void onInvisible() {

    }

    /**
     * 一些初始化操作
     */
    protected abstract void init();

    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 是否隐藏底部导航栏
     *
     * @return
     */
    protected abstract boolean hideBottom();

    /**
     * 处理 handler 的消息
     *
     * @param msg
     */
    protected abstract void handleMsg(Message msg);

    /**
     * 显示进度条
     */
    protected void showProgress() {
        if (customProgressView == null) {
            customProgressView = new CustomProgressView(getActivity())
                    .setCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
        }
        customProgressView.showProgressDialog();
    }

    /**
     * 清理进度条
     */
    protected void clearProgress() {
        if (customProgressView != null) {
            customProgressView.dissDialog();
            customProgressView = null;
        }
    }

    public static class Myhandler extends Handler {
    }

}
