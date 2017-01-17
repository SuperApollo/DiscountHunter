package com.apollo.discounthunter.ui.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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
    private long mExitTime;
    private List<Fragment> mFragments;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
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
        //隐藏viewpager里的fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : mFragments) {
            transaction.hide(fragment);
        }
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
        transaction.commit();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
