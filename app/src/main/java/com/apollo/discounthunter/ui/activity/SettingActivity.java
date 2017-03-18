package com.apollo.discounthunter.ui.activity;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.utils.DataCleanUtil;
import com.apollo.discounthunter.utils.ImageLoaderUtils;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.utils.MyPopUtil;
import com.apollo.discounthunter.utils.SharedPreferencesUtils;
import com.apollo.discounthunter.widgets.ItemView;
import com.apollo.discounthunter.widgets.MyProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by apollo on 17-3-14.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.item_setting_about)
    ItemView itemAbout;
    @BindView(R.id.item_setting_flash)
    ItemView itemFlash;
    @BindView(R.id.item_setting_clear_cache)
    ItemView itemClearCache;
    private MyPopUtil mMyPopUtil;
    private MyProgressDialog clearProgressDialog;
    private final int CLEAR_SUCCESS = 0x0001;
    private final int DISMISS_DIALOG = 0x0002;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLEAR_SUCCESS:
                    clearProgressDialog.dismiss();
                    //刷新缓存大小
                    getCahceSize();
                    clearProgressDialog = new MyProgressDialog(SettingActivity.this, R.style.NoWhiteDialog, R.layout.load_dialog_done);
                    clearProgressDialog.show();
                    mHandler.sendEmptyMessageDelayed(DISMISS_DIALOG, 1000);
                    break;
                case DISMISS_DIALOG:
                    clearProgressDialog.dismiss();
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mMyPopUtil != null) {
            mMyPopUtil.dismiss();
            mMyPopUtil.destory();
            System.gc();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyPopUtil = MyPopUtil.getInstance(SettingActivity.this);
//        mMyPopUtil.updateContext(SettingActivity.this);
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
        itemAbout.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                IntentUtils.sendIntent(SettingActivity.this, AboutActivity.class);
            }
        });
        itemFlash.setToggleButtonChangeListner(new ItemView.OnToggleButtonChangeListner() {
            @Override
            public void onToggleChanged(boolean on) {
                mToastUtils.show(SettingActivity.this, on + "");
            }
        });

        itemClearCache.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                if (!TextUtils.equals("0KB", itemClearCache.getTvRight().getText())) {
                    mMyPopUtil.initView(R.layout.clear_cache_pop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                            R.style.add_pop_tv_style);
                    mMyPopUtil.showAtLoacation(view, Gravity.CENTER, 0, 0);
                    mMyPopUtil.getmPopView().findViewById(R.id.tv_clear_cache_pop_cancel).setOnClickListener(SettingActivity.this);
                    mMyPopUtil.getmPopView().findViewById(R.id.tv_clear_cache_pop_ok).setOnClickListener(SettingActivity.this);
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

        getCahceSize();

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
                mMyPopUtil.dismiss();
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

                mMyPopUtil.dismiss();
                break;
        }
    }

}
