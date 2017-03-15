package com.apollo.discounthunter.ui.activity;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.AppUpdateInfoModel;
import com.apollo.discounthunter.utils.AppUtil;
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
    private static final int SERVER_VERSION_ERROR = 0x00010086;
    private static final int HAS_UPDATE = 0x00010087;
    private static final int NO_UPDATE = 0x00010088;
    @BindView(R.id.item_about_update)
    ItemView itemUpdate;
    @BindView(R.id.item_about_suggestion)
    ItemView itemSuggestion;
    @BindView(R.id.tv_about_version)
    TextView tvVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView() {
        String versionName = AppUtil.getAppVersionName(this);
        tvVersion.setText("折扣猎手 V" + versionName);
        itemUpdate.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                checkUpdate();
            }
        });

        itemSuggestion.setOnItemClickedListner(new ItemView.onItemClickedListner() {
            @Override
            public void onClick() {
                mToastUtils.show(mContext, "意见反馈");
            }
        });
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
            if (json != null)
                updateInfoModel = gson.fromJson(json, AppUpdateInfoModel.class);
            String serverVersion = updateInfoModel.getAppVersion();
            String localVersion = AppUtil.getAppVersionName(mContext);
            int toUpdate = toUpdate(serverVersion, localVersion);
            switch (toUpdate) {
                case HAS_UPDATE:
                    mToastUtils.show(mContext, "新版本" + serverVersion);
                    break;
                case NO_UPDATE:
                    mToastUtils.show(mContext, "当前是最新版本");
                    break;
                case SERVER_VERSION_ERROR:
                    mToastUtils.show(mContext, "服务器版本号错误");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (updateInfoModel != null)
            Log.i(TAG, updateInfoModel.toString());
    }

    private class CheckUpdateTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                //从URL加载document对象
                Document document = Jsoup.connect(Constants.CHECK_UPDATE_GITHUB_URL).get();
                Element body = document.body();
                if (body != null)
                    parseData(body.text());

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
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

}
