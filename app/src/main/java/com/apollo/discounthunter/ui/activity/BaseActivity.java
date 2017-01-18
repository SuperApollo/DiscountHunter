package com.apollo.discounthunter.ui.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SearchView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.base.BaseApplication;
import com.apollo.discounthunter.utils.ActivityManager;
import com.apollo.discounthunter.utils.ToastUtils;
import com.apollo.discounthunter.widgets.CustomProgressView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;

/**
 * Created by zayh_yf20160909 on 2017/1/11.
 */

public abstract class BaseActivity extends FragmentActivity implements MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener {
    protected ToastUtils mToastUtils;
    Context mContext;
    protected CustomProgressView customProgressView;
    protected MenuItem mSearchItem;//顶部搜索
    protected final String TAG = getClass().getSimpleName();
    protected ActionBar mActionBar;
    protected SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setOverflowShowingAlways();
        mContext = BaseApplication.getContext();
        mToastUtils = ToastUtils.shareInstance();
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.mipmap.icon_arrow_left);
        initView();
        ActivityManager.getInstance().addActivity(BaseActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int id = getMenuLayoutId();
        if (id != -1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(id, menu);
            mSearchItem = menu.findItem(R.id.action_search);
            mSearchItem.setOnActionExpandListener(this);
            mSearchView = (SearchView) mSearchItem.getActionView();
            if (mSearchView != null)
                mSearchView.setOnQueryTextListener(this);
            return true;
        } else
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!TextUtils.equals("MainActivity", getClass().getSimpleName())) {//mainactivity不响应点击
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //解决activity退出动画不生效问题
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.equals("MainActivity", getClass().getSimpleName())) {
                this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       return false;
    }

}
