package com.apollo.discounthunter.ui.activity;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.ui.fragment.FragmentAdapter;
import com.apollo.discounthunter.ui.fragment.HomeFragment;
import com.apollo.discounthunter.ui.fragment.HotFragment;
import com.apollo.discounthunter.ui.fragment.RecommendFragment;
import com.apollo.discounthunter.utils.ViewUtil;

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
    private ActionBar mActionBar;
    private long mExitTime;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuLayoutId() {
        return R.menu.main;
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
        mActionBar = getActionBar();
        initFragment();
        setTitle(R.string.item_home);
        mRbHome.setChecked(true);

    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        RecommendFragment recommendFragment = new RecommendFragment();
        HotFragment hotFragment = new HotFragment();
        fragments.add(homeFragment);
        fragments.add(recommendFragment);
        fragments.add(hotFragment);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
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
                break;
            case 1:
                mRbHome.setChecked(false);
                mRbRecommend.setChecked(true);
                mRbHot.setChecked(false);
                break;
            case 2:
                mRbHome.setChecked(false);
                mRbRecommend.setChecked(false);
                mRbHot.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        mRadioGroup.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        mRadioGroup.setVisibility(View.VISIBLE);
        return true;
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
}
