package com.apollo.discounthunter.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewConfiguration;
import android.view.Window;

import com.apollo.discounthunter.base.BaseApplication;
import com.apollo.discounthunter.utils.ToastUtils;
import com.apollo.discounthunter.widgets.CustomProgressView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;

/**
 * Created by zayh_yf20160909 on 2017/1/11.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected ToastUtils mToastUtils;
    Context mContext;
    protected CustomProgressView customProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setOverflowShowingAlways();
        mContext = BaseApplication.getContext();
        mToastUtils = ToastUtils.shareInstance();
        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int id = getMenuLayoutId();
        if (id != -1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(id, menu);
            return true;
        } else
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //利用反射获取menubuilder类，调用setOptionalIconsVisible方法设置为true，显示overflow里的action按钮的图标
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 使overflowbutton始终显示
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取布局id,之类若没有布局需返回-1
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 获取actionbar的menu布局
     *
     * @return menu布局资源id
     */
    protected abstract int getMenuLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 显示进度条
     */
    protected void showProgress() {
        if (customProgressView == null) {
            customProgressView = new CustomProgressView(mContext)
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
}
