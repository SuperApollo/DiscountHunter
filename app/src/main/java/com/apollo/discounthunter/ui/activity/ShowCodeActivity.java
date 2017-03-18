package com.apollo.discounthunter.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.apollo.discounthunter.R;
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
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Bitmap www = EncodingHandler.createQRCode("www.baidu.com", 600, 600, bitmap);
            ivShowCode.setImageBitmap(www);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
