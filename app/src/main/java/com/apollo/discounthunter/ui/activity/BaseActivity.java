package com.apollo.discounthunter.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.SearchView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.base.BaseApplication;
import com.apollo.discounthunter.utils.ToastUtils;
import com.apollo.discounthunter.widgets.CustomProgressView;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by zayh_yf20160909 on 2017/1/11.
 */

public abstract class BaseActivity extends FragmentActivity implements MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener,BGASwipeBackHelper.Delegate {
    protected ToastUtils mToastUtils;
    public Context mContext;
    protected CustomProgressView customProgressView;
    protected MenuItem mSearchItem;//顶部搜索
    protected final String TAG = getClass().getSimpleName();
    protected ActionBar mActionBar;
    protected SearchView mSearchView;
    public EditText mSearchEditText;

    protected MyHandler mHandler = new MyHandler() {
        @Override
        public void handleMessage(Message msg) {
            handleMsg(msg);
        }
    };
    private Unbinder mUnbinder;
    private BGASwipeBackHelper mSwipeBackHelper;

    public EditText getmSearchEditText() {
        return mSearchEditText;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(view);
        mUnbinder = ButterKnife.bind(this);
        setOverflowShowingAlways();
        mContext = BaseApplication.getContext();
        mToastUtils = ToastUtils.shareInstance();
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.mipmap.icon_arrow_left);
        initView(view);
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(BaseActivity.this);
//        ActivityManager.getInstance().addActivity(activityWeakReference.get());
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
        setStatusBarColor(color, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//统计时长
        MobclickAgent.onPageStart(this.getClass().getSimpleName());//统计页面
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mUnbinder.unbind();
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
            if (mSearchView != null) {
                mSearchView.setOnQueryTextListener(this);
                int viewId = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
                mSearchEditText = (EditText) mSearchView.findViewById(viewId);
                //去掉edittext下划线
                try {
                    Class argClass = mSearchView.getClass();
                    // mSearchPlate是SearchView父布局的名字
                    Field ownField = argClass.getDeclaredField("mSearchPlate");
                    ownField.setAccessible(true);
                    View mView = (View) ownField.get(mSearchView);
                    mView.setBackgroundColor(Color.TRANSPARENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }

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
     *
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 设置标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(title);
    }

    /**
     * 处理 handler 消息
     *
     * @param msg
     */
    protected abstract void handleMsg(Message msg);

    /**
     * 显示进度条
     */
    protected void showProgress() {
        if (customProgressView == null) {
            customProgressView = new CustomProgressView(this)
                    .setCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
        }
        customProgressView.setTimeout(60);
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

    protected <T extends View> T queryViewById(View parentView, int id) {
        return (T) parentView.findViewById(id);
    }

    public static class MyHandler extends Handler {

    }
}
