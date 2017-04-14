package com.apollo.discounthunter.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.AppUpdateInfoModel;
import com.apollo.discounthunter.ui.fragment.FragmentAdapter;
import com.apollo.discounthunter.ui.fragment.HomeFragment;
import com.apollo.discounthunter.ui.fragment.HotFragment;
import com.apollo.discounthunter.ui.fragment.RecommendFragment;
import com.apollo.discounthunter.ui.fragment.SearchFragment;
import com.apollo.discounthunter.utils.ApkUpdateUtil;
import com.apollo.discounthunter.utils.AppUtil;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.utils.MyPopUtil;
import com.apollo.discounthunter.utils.SharedPreferencesUtils;
import com.apollo.discounthunter.utils.ViewUtil;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
    @BindView(R.id.btn_main_to_top)
    Button mBtnToTop;
    @BindView(R.id.btn_main_to_bottom)
    Button mBtnToBottom;

    public Button getmBtnToTop() {
        return mBtnToTop;
    }

    public Button getmBtnToBottom() {
        return mBtnToBottom;
    }

    private long mExitTime;
    private List<Fragment> mFragments;
    private SearchFragment mSearchFragment;
    private FragmentAdapter mFragmentAdapter;
    private OnSearchListner onSearchListner;
    private String mEid = "0";//记录当前在那个fragment页面，告诉搜索页
    private final int CHECK_UPDATE = 1;
    private final int MY_PERMISSIONS_REQUEST = 2;
    private final int SERVER_VERSION_ERROR = 0x00010086;
    private final int HAS_UPDATE = 0x00010087;
    private final int NO_UPDATE = 0x00010088;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_UPDATE:
                    checkUpdate();
                    break;
            }

        }
    };
    private View parent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

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
    protected void initView(View view) {
        parent = view;
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
        Message message = new Message();
        message.what = CHECK_UPDATE;
        mHandler.sendMessageDelayed(message, 1000);

        checkPermission();
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

    /**
     * 检查更新
     */
    private void checkUpdate() {
        new CheckUpdateTask().execute();
    }

    private class CheckUpdateTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgress();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String json = null;
            try {
                //从URL加载document对象
                Document document = Jsoup.connect(Constants.CHECK_UPDATE_GITHUB_URL).get();
                Element body = document.body();
                json = body.text();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return json;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            parseData((String) o);
            clearProgress();
        }
    }

    private void parseData(String json) {
        AppUpdateInfoModel updateInfoModel = null;
        try {
            Gson gson = new Gson();
            if (json != null)
                updateInfoModel = gson.fromJson(json, AppUpdateInfoModel.class);
            String serverVersion = updateInfoModel.getAppVersion();
            String localVersion = AppUtil.getAppVersionName(mContext);
            SharedPreferencesUtils.putString(AppConfig.APK_URL, updateInfoModel.getAppUrl());
            int toUpdate = toUpdate(serverVersion, localVersion);
            switch (toUpdate) {
                case HAS_UPDATE:
                    chooseDialogShow(serverVersion, updateInfoModel.getAppUrl(), updateInfoModel.getAppSize(), updateInfoModel.getAppDescription());
                    SharedPreferencesUtils.putBoolean(AppConfig.HAS_UPDATE, true);
                    break;
                case NO_UPDATE:
                    mToastUtils.show(mContext, "当前已是最新版本");
                    SharedPreferencesUtils.putBoolean(AppConfig.HAS_UPDATE, false);
                    break;
                case SERVER_VERSION_ERROR:
                    mToastUtils.show(mContext, "服务器版本号错误");
                    SharedPreferencesUtils.putBoolean(AppConfig.HAS_UPDATE, false);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否升级
     *
     * @param appVersion
     * @return
     */
    private int toUpdate(String appVersion, String currentVersionName) {

        if (!TextUtils.isEmpty(appVersion) && !TextUtils.isEmpty(currentVersionName)) {
            String[] versionStr = new String[]{
                    appVersion, currentVersionName
            };
            int[] versionInt = versionName2Int(versionStr);

            if (versionInt[0] == -1) {
                return SERVER_VERSION_ERROR;
            } else if (versionInt[0] > versionInt[1]) {
                return HAS_UPDATE;
            } else {
                return NO_UPDATE;
            }
        }
        return NO_UPDATE;
    }

    /**
     * 版本号转换为int，便于比较
     *
     * @param versions
     * @return
     */
    private int[] versionName2Int(String[] versions) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        String[] s = versions[0].split("\\.");
        String[] s1 = versions[1].split("\\.");
        for (String ss : s) {
            sb.append(ss);
        }
        for (String ss1 : s1) {
            sb1.append(ss1);
        }
        int var = sb.length() - sb1.length();
        if (var > 0) {
            for (int i = 0; i < var; i++) {
                sb1.append("0");
            }
        } else if (var < 0) {
            for (int i = 0; i < -var; i++) {
                sb.append("0");
            }
        }

        int versino1 = -1;
        int version2 = -1;
        try {
            versino1 = Integer.parseInt(sb.toString());
            version2 = Integer.parseInt(sb1.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        int[] versionsInt = new int[]{versino1, version2};

        return versionsInt;
    }

    /**
     * 是否升级弹框
     *
     * @param curVersion
     * @param appUrl
     * @param appSize
     * @param appDescription
     */
    private void chooseDialogShow(String curVersion, final String appUrl, String appSize, String appDescription) {
        MyPopUtil myPopUtil = MyPopUtil.getInstance(MainActivity.this);
        myPopUtil.initView(R.layout.new_update_pop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                R.style.add_pop_tv_style);
        if (parent == null)
            return;
        myPopUtil.showAtLoacation(parent, Gravity.CENTER, 0, 0);
        TextView tv_new_update_num = queryViewById(myPopUtil.getmPopView(), R.id.tv_new_update_num);
        tv_new_update_num.setText("版本号：" + curVersion);
        TextView tvDescription = queryViewById(myPopUtil.getmPopView(), R.id.tv_new_update_description);
        tvDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        LinearLayout ll_new_update_description = queryViewById(myPopUtil.getmPopView(), R.id.ll_new_update_description);
        if (!TextUtils.isEmpty(appDescription)) {
            tvDescription.setText(appDescription + "\n【更新包大小】" + appSize);
            ll_new_update_description.setVisibility(View.VISIBLE);
        }

        TextView tv_cancel = queryViewById(myPopUtil.getmPopView(), R.id.tv_new_update_pop_cancel);
        TextView tv_ok = queryViewById(myPopUtil.getmPopView(), R.id.tv_new_update_pop_ok);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopUtil.getInstance(MainActivity.this).dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopUtil.getInstance(MainActivity.this).dismiss();
                try {//防止apprUrl错误造成崩溃
                    ApkUpdateUtil apkUpdateUtil = new ApkUpdateUtil(MainActivity.this, appUrl);
                    apkUpdateUtil.startDown();
                } catch (Exception e) {
                    e.printStackTrace();
                    mToastUtils.show(mContext, e.getMessage());
                }
            }
        });
    }

    /**
     * 权限检查
     */
    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO//麦克风权限
                    , Manifest.permission.CAMERA//摄像头权限
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);//读写sd卡权限
        }

    }

}
