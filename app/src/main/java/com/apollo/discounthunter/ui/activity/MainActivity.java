package com.apollo.discounthunter.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.ui.fragment.FragmentAdapter;
import com.apollo.discounthunter.ui.fragment.HomeFragment;
import com.apollo.discounthunter.ui.fragment.HotFragment;
import com.apollo.discounthunter.ui.fragment.RecommendFragment;
import com.apollo.discounthunter.ui.fragment.SearchFragment;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.utils.LogUtil;
import com.apollo.discounthunter.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.rg_main_bottom)
    RadioGroup mRadioGroup;
    @BindView(R.id.rb_home)
    RadioButton mRbHome;
    @BindView(R.id.rb_recommend)
    RadioButton mRbRecommend;
    @BindView(R.id.rb_hot)
    RadioButton mRbHot;
    @BindView(R.id.vp_main_container)
    ViewPager mVpContainer;
    @BindView(R.id.fl_main_container)
    FrameLayout mFlContainer;

    private long mExitTime;
    private List<Fragment> mFragments;
    private SearchFragment mSearchFragment;
    private FragmentAdapter mFragmentAdapter;
    private OnSearchListner onSearchListner;
    private String mEid = "0";//记录当前在那个fragment页面，告诉搜索页

    public String getmEid() {
        return mEid;
    }

    public void setmEid(String mEid) {
        this.mEid = mEid;
    }

    public void setOnSearchListner(OnSearchListner onSearchListner) {
        this.onSearchListner = onSearchListner;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuLayoutId() {
        return R.menu.main;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);//禁止默认的activity 统计方式
    }

    @Override
    protected void initView() {
        //设置radiobutton上方图片的大小
        Drawable homeDrawable = getResources().getDrawable(R.drawable.selector_item_home);
        homeDrawable.setBounds(0, 0, ViewUtil.dp2px(mContext, 24), ViewUtil.dp2px(mContext, 24));
        mRbHome.setCompoundDrawables(null, homeDrawable, null, null);
        Drawable recommendDrawable = getResources().getDrawable(R.drawable.selector_item_recommend);
        recommendDrawable.setBounds(0, 0, ViewUtil.dp2px(mContext, 24), ViewUtil.dp2px(mContext, 24));
        mRbRecommend.setCompoundDrawables(null, recommendDrawable, null, null);
        Drawable hotDrawable = getResources().getDrawable(R.drawable.selector_item_hot);
        hotDrawable.setBounds(0, 0, ViewUtil.dp2px(mContext, 24), ViewUtil.dp2px(mContext, 24));
        mRbHot.setCompoundDrawables(null, hotDrawable, null, null);

        mVpContainer.setOffscreenPageLimit(2);
        mRadioGroup.setOnCheckedChangeListener(this);
        initFragment();
        setTitle(R.string.item_home);
        mRbHome.setChecked(true);

        mActionBar.setDisplayHomeAsUpEnabled(false);

    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        mFragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        RecommendFragment recommendFragment = new RecommendFragment();
        HotFragment hotFragment = new HotFragment();
        mFragments.add(homeFragment);
        mFragments.add(recommendFragment);
        mFragments.add(hotFragment);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        mVpContainer.setAdapter(mFragmentAdapter);
        mVpContainer.setCurrentItem(0);
        mVpContainer.setOnPageChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_home:
                setTitle(R.string.item_home);
                mVpContainer.setCurrentItem(0);
                break;
            case R.id.rb_recommend:
                setTitle(R.string.item_recommend);
                mVpContainer.setCurrentItem(1);
                break;
            case R.id.rb_hot:
                setTitle(R.string.item_hot);
                mVpContainer.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRbHome.setChecked(true);
                mRbRecommend.setChecked(false);
                mRbHot.setChecked(false);
                mEid = "0";
                break;
            case 1:
                mRbHome.setChecked(false);
                mRbRecommend.setChecked(true);
                mRbHot.setChecked(false);
                mEid = "2";
                break;
            case 2:
                mRbHome.setChecked(false);
                mRbRecommend.setChecked(false);
                mRbHot.setChecked(true);
                mEid = "6";
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        mRadioGroup.setVisibility(View.GONE);
        //隐藏viewpager里的fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : mFragments) {
            transaction.hide(fragment);
        }
        //添加搜索fragment
        if (mSearchFragment == null)
            mSearchFragment = new SearchFragment();

        mVpContainer.setVisibility(View.GONE);
        mFlContainer.setVisibility(View.VISIBLE);
        transaction.add(R.id.fl_main_container, mSearchFragment);
        transaction.show(mSearchFragment);
        transaction.commit();

        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        mRadioGroup.setVisibility(View.VISIBLE);
        //显示
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : mFragments) {
            transaction.show(fragment);
        }

        //移除搜索fragment
        if (mSearchFragment != null) {
            transaction.remove(mSearchFragment);
        }
        transaction.commit();
        mSearchFragment = null;
        mVpContainer.setVisibility(View.VISIBLE);
        mFlContainer.setVisibility(View.GONE);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (onSearchListner != null) {
            return onSearchListner.onSearchTextSubmit(query);
        } else {
            return super.onQueryTextSubmit(query);
        }

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (onSearchListner != null) {
            return onSearchListner.onSearchTextChange(newText);
        } else {
            return super.onQueryTextChange(newText);
        }
    }

    @Override
    public void onBackPressed() {
        if ((currentTimeMillis() - mExitTime) > 2000) {
            mToastUtils.show(mContext, "再按一次退出程序");
            mExitTime = currentTimeMillis();
        } else {
            MainActivity.this.finish();
        }
    }

    public interface OnSearchListner {
        boolean onSearchTextSubmit(String text);

        boolean onSearchTextChange(String newText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                IntentUtils.sendIntent(this, SettingActivity.class);
                return true;
            case R.id.action_plus:
                mToastUtils.show(this, "添加");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
