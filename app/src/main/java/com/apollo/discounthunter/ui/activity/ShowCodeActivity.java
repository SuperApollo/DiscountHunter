package com.apollo.discounthunter.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.utils.SharedPreferencesUtils;
import com.apollo.discounthunter.zxing.encoding.EncodingHandler;

import butterknife.BindView;

/**
 * 展示二维码
 * Created by apollo on 17-3-18.
 */

public class ShowCodeActivity extends BaseActivity {
    @BindView(R.id.iv_show_code)
    ImageView ivShowCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_code;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        try {
//                    Bitmap mBitmap = EncodingHandler.createQRCode("www.baidu.com", 300);
//                    qrcodeImg.setImageBitmap(mBitmap);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo_trans);
            String url = SharedPreferencesUtils.getString(AppConfig.APK_URL);
            Bitmap www = EncodingHandler.createQRCode(url, 600, 600, bitmap);
            ivShowCode.setImageBitmap(www);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMsg(Message msg) {

    }
}
