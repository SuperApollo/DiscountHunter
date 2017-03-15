package com.apollo.discounthunter.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.widgets.HorizontalProgressBar;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * APP升级工具
 * Created by apollo on 17-3-15.
 */

public class ApkUpdateUtil {
    private static final String savaPath = AppConfig.FILE_DOWNLOAD + "apk";
    private static final String apkName = "discounthunter.apk";
    private String apkUrl;
    private Context mContext;
    private HorizontalProgressBar bar;
    private Dialog dialog;
    private TextView textSize;

    public ApkUpdateUtil(Context context, String apkUrl) {
        this.apkUrl = apkUrl;
        this.mContext = context;
        initDialog();
    }

    private void initDialog() {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.view_progressbar_horizontal);
        bar = (HorizontalProgressBar) dialog.findViewById(R.id.my_progress);
        textSize = (TextView) dialog.findViewById(R.id.text_update_size);
    }

    private void updatePress(long total, long current) {
        if (total <= 0) {
            return;
        }
        double press = ((current * 0.1d) / (total * 0.1d)) * 100;
        bar.setProgress((int) press);
        textSize.setText("正在更新" + (int) press + "%");
    }

    public void startDown() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断是否存在SD卡
            ToastUtils.show(mContext, "SD卡不可用");
            return;
        }
        final File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "discount_hunter" + File.separator + apkName);
        if (!file.getParentFile().exists()) {// 判断父文件是否存在，如果不存在则创建
            file.getParentFile().mkdirs();
        }
        //        if (file.exists()) {
        //            setupAPk(file);
        //            return;
//        }
//        OkHttpManager.getInstance().down(apkUrl, savaPath, apkName).enqueue(new IProgressCallback() {
//            @Override
//            public void progress(long total, long current) {
//                updatePress(total, current);
//            }
//
//            @Override
//            public boolean before() {
//                dialog.show();
//                return false;
//            }
//
//            @Override
//            public void success(String result) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (file.exists()) {
//                    setupAPk(file);
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                call.cancel();
//                Toast.makeText(mContext.getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        });
    }


    private void setupAPk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}
