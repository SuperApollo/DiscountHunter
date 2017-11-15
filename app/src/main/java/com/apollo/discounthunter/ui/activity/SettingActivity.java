package com.apollo.discounthunter.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.collection.view.MyCollectionActivity;
import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.flash.Flash;
import com.apollo.discounthunter.flash.FlashLightManager;
import com.apollo.discounthunter.utils.DataCleanUtil;
import com.apollo.discounthunter.utils.ImageLoaderUtils;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.utils.MyPopUtil;
import com.apollo.discounthunter.utils.SharedPreferencesUtils;
import com.apollo.discounthunter.utils.ToastUtils;
import com.apollo.discounthunter.widgets.ItemView;
import com.apollo.discounthunter.widgets.MyProgressDialog;
import com.apollo.discounthunter.zxing.activity.CaptureActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by apollo on 17-3-14.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.item_setting_my_collection)
    ItemView itemCollection;
    @BindView(R.id.item_setting_about)
    ItemView itemAbout;
    @BindView(R.id.item_setting_flash)
    ItemView itemFlash;
    @BindView(R.id.item_setting_clock)
    ItemView itemClock;
    @BindView(R.id.item_setting_clear_cache)
    ItemView itemClearCache;
    @BindView(R.id.item_setting_my_code)
    ItemView itemMyCode;
    @BindView(R.id.item_setting_scan)
    ItemView itemScan;
    @BindView(R.id.item_setting_share)
    ItemView itemShare;

    private MyProgressDialog clearProgressDialog;
    private final int CLEAR_SUCCESS = 0x0001;
    private final int DISMISS_DIALOG = 0x0002;
    private final int REQUEST_CODE_SCAN = 1;
    private boolean mUpper;
    private FlashLightManager mFlashLightManager;
    private Flash mFlash;
    private ShareAction mShareAction;


    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mContext, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, "分享取消", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //友盟分享内存泄露
        UMShareAPI.get(this).release();
        if (mFlashLightManager != null) {
            mFlashLightManager.releaseResource();
            mFlashLightManager = null;
        }

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(final View view) {
        setTitle("我的设置");
        itemCollection.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                IntentUtils.sendIntent(SettingActivity.this, MyCollectionActivity.class);
            }
        });
        itemAbout.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                IntentUtils.sendIntent(SettingActivity.this, AboutActivity.class);
            }
        });
        itemFlash.setToggleButtonChangeListner(new ItemView.OnToggleButtonChangeListner() {
            @Override
            public void onToggleChanged(boolean on) {
                if (mUpper) {
                    mFlashLightManager.setFlashlight(on);
                } else {
                    mFlash.setLight(on);
                }
            }
        });
        itemClock.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                IntentUtils.sendIntent(SettingActivity.this, ShowClockActivity.class);
            }
        });

        itemClearCache.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                if (!TextUtils.equals("0KB", itemClearCache.getTvRight().getText())) {
                    MyPopUtil myPopUtil = MyPopUtil.getInstance(SettingActivity.this);
                    myPopUtil.initView(R.layout.clear_cache_pop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                            R.style.add_pop_tv_style);
                    myPopUtil.showAtLoacation(view, Gravity.CENTER, 0, 0);
                    myPopUtil.getmPopView().findViewById(R.id.tv_clear_cache_pop_cancel).setOnClickListener(SettingActivity.this);
                    myPopUtil.getmPopView().findViewById(R.id.tv_clear_cache_pop_ok).setOnClickListener(SettingActivity.this);
                } else {//没有缓存
                    clearProgressDialog = new MyProgressDialog(SettingActivity.this, R.style.NoWhiteDialog, R.layout.load_dialog_done);
                    clearProgressDialog.show();
                    mHandler.sendEmptyMessageDelayed(DISMISS_DIALOG, 1000);
                }
            }
        });

        boolean hasUpdate = SharedPreferencesUtils.getBoolean(AppConfig.HAS_UPDATE, false);
        if (hasUpdate) {
            itemAbout.getIvPoint().setVisibility(View.VISIBLE);
        }

        itemMyCode.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                IntentUtils.sendIntent(SettingActivity.this, ShowCodeActivity.class);
            }
        });

        itemScan.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                Intent intent = new Intent(SettingActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        itemShare.setOnItemClickedListner(new ItemView.onItemClickedListner() {


            @Override
            public void onClick() {
                UMWeb web = new UMWeb(Constants.zkls_tencent_url);
                UMImage thumb = new UMImage(mContext, Constants.zkls_tencent_pic);
                web.setTitle("好货推荐");//标题
                web.setThumb(thumb);  //缩略图
                web.setDescription("我发现了一个有趣的APP，推荐给你哦");//描述
                mShareAction = new ShareAction(SettingActivity.this);
                ShareBoardConfig config = new ShareBoardConfig();
                config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
                mShareAction.withMedia(web)
                        .setDisplayList(
                                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                                SHARE_MEDIA.SINA

                        )
                        .setCallback(shareListener)
                        .open(config);

            }
        });

        getCahceSize();

        initFlash();

    }

    @Override
    protected void handleMsg(Message msg) {
        WeakReference<SettingActivity> reference = new WeakReference<>(SettingActivity.this);
        switch (msg.what) {
            case CLEAR_SUCCESS:
                clearProgressDialog.dismiss();
                //刷新缓存大小
                getCahceSize();
                clearProgressDialog = new MyProgressDialog(reference.get(), R.style.NoWhiteDialog, R.layout.load_dialog_done);
                clearProgressDialog.show();
                mHandler.sendEmptyMessageDelayed(DISMISS_DIALOG, 1000);
                break;
            case DISMISS_DIALOG:
                clearProgressDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            // TODO: 2017/2/28 进webview
            IntentUtils.sendIntent(SettingActivity.this, ShowWebActivity.class, bundle);

        }
    }

    /**
     * 获取缓存大小
     */
    private void getCahceSize() {
        List<File> files = new ArrayList<>();
        File disDir = new File(AppConfig.FILE_DOWNLOAD);
        File disFile = new File(Environment.getExternalStorageDirectory().getPath() + getPackageName());
        files.add(disDir);
        files.add(ImageLoaderUtils.getInstance(mContext).getDiskCache());
        files.add(disFile);
        try {
            String size = DataCleanUtil.getCacheSize(files);
            itemClearCache.getTvRight().setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    /**
     * 清理缓存
     */
    private void clearCache() {
        //清除下载文件夹缓存
        DataCleanUtil.cleanCustomCache(AppConfig.FILE_DOWNLOAD);
        //清除imageloader缓存
        ImageLoaderUtils.getInstance(mContext).clearCache();
        //清理融云缓存
        File file = new File(Environment.getExternalStorageDirectory().getPath() + getPackageName());
        deleteFile(file);
    }

    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear_cache_pop_cancel:
                MyPopUtil.getInstance(SettingActivity.this).dismiss();
                break;
            case R.id.tv_clear_cache_pop_ok:
                clearProgressDialog = new MyProgressDialog(this, R.style.NoWhiteDialog);
                clearProgressDialog.show();

                /**
                 * 异步清缓存
                 */
                new AsyncTask() {

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        clearCache();
                        mHandler.sendEmptyMessageDelayed(CLEAR_SUCCESS, 500);

                        return null;
                    }
                }.execute();

                MyPopUtil.getInstance(SettingActivity.this).dismiss();
                break;
        }
    }

    /**
     * 闪光灯初始化
     */
    private void initFlash() {

        mUpper = isVersion21Upper();
        if (mUpper) {
            mFlashLightManager = new FlashLightManager(mContext);
        } else {
            mFlash = Flash.getInstance();
            mFlash.open();
        }

    }

    //判断版本是否是5.0以上
    private boolean isVersion21Upper() {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return true;
        }
        return false;
    }


}
