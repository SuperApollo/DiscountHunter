package com.apollo.discounthunter.ui.activity;

import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.AppUpdateInfoModel;
import com.apollo.discounthunter.utils.ApkUpdateUtil;
import com.apollo.discounthunter.utils.AppUtil;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.utils.MyPopUtil;
import com.apollo.discounthunter.utils.SharedPreferencesUtils;
import com.apollo.discounthunter.utils.ToastUtils;
import com.apollo.discounthunter.widgets.ItemView;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by apollo on 17-3-14.
 */

public class AboutActivity extends BaseActivity {
    private final int SERVER_VERSION_ERROR = 0x00010086;
    private final int HAS_UPDATE = 0x00010087;
    private final int NO_UPDATE = 0x00010088;
    @BindView(R.id.item_about_update)
    ItemView itemUpdate;
    @BindView(R.id.item_about_suggestion)
    ItemView itemSuggestion;
    @BindView(R.id.tv_about_version)
    TextView tvVersion;
    private View parent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        parent = view;
        String versionName = AppUtil.getAppVersionName(this);
        tvVersion.setText("折扣猎手 V" + versionName);
        itemUpdate.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                checkUpdate();
            }
        });

        boolean hasUpdate = SharedPreferencesUtils.getBoolean(AppConfig.HAS_UPDATE, false);
        if (hasUpdate) {
            itemUpdate.getIvPoint().setVisibility(View.VISIBLE);
        }else {
            itemUpdate.getIvPoint().setVisibility(View.GONE);
        }

        itemSuggestion.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                IntentUtils.sendIntent(AboutActivity.this, FeedbackActivity.class);
            }
        });

    }

    @Override
    protected void handleMsg(Message msg) {

    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        new CheckUpdateTask().execute();

    }

    private void parseData(String json) {
        AppUpdateInfoModel updateInfoModel = null;
        try {
            Gson gson = new Gson();
            if (json != null){
                updateInfoModel = gson.fromJson(json, AppUpdateInfoModel.class);
            }

            if (updateInfoModel == null) {
                ToastUtils.show(mContext, "服务器开小差");
                return;
            }
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
                    ToastUtils.show(mContext, "当前已是最新版本");
                    SharedPreferencesUtils.putBoolean(AppConfig.HAS_UPDATE, false);
                    break;
                case SERVER_VERSION_ERROR:
                    ToastUtils.show(mContext, "服务器版本号错误");
                    SharedPreferencesUtils.putBoolean(AppConfig.HAS_UPDATE, false);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class CheckUpdateTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
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
        MyPopUtil myPopUtil = MyPopUtil.getInstance(this);
        myPopUtil.initView(R.layout.new_update_pop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                R.style.add_pop_tv_style);
        if (parent == null){
            return;
        }

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
                MyPopUtil.getInstance(AboutActivity.this).dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopUtil.getInstance(AboutActivity.this).dismiss();
                try {//防止apprUrl错误造成崩溃
                    ApkUpdateUtil apkUpdateUtil = new ApkUpdateUtil(AboutActivity.this, appUrl);
                    apkUpdateUtil.startDown();
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.show(mContext, e.getMessage());
                }
            }
        });
    }

}
