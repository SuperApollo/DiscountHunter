package com.apollo.discounthunter.ui.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.Model;
import com.apollo.discounthunter.utils.ImageLoaderUtils;

import butterknife.BindView;

/**
 * 商品详情展示页
 * Created by wangpengbo on 2017/1/15.
 */

public class GoodsDetailActivity extends BaseActivity {
    @BindView(R.id.tv_goods_detail_title)
    TextView mTvTitle;
    @BindView(R.id.iv_goods_detail)
    ImageView mIv;
    @BindView(R.id.tv_goods_detail_reason)
    TextView mTvReasson;
    @BindView(R.id.tv_goods_detail_price)
    TextView mTvPrice;
    @BindView(R.id.btn_goods_detail_quan)
    Button mBtnQuan;
    @BindView(R.id.btn_goods_detail_buy)
    Button mBtnBuy;

    private Model mHomeModel;
    private ImageLoaderUtils mImageLoader;
    private ActionBar mActionBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView() {
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mImageLoader = ImageLoaderUtils.getInstance(mContext);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            mHomeModel = bundle.getParcelable(Constants.GOODS_INFO);
            String title = mHomeModel.getTitle();
            if (!TextUtils.equals("0", mHomeModel.getFlag()))
                title = "【置顶】" + title;
            setTitle(title);
            mTvTitle.setText(title);
            mTvReasson.setText(mHomeModel.getReason());
            mImageLoader.loadImageView(mHomeModel.getPic(),mIv);
            float p = Float.parseFloat(mHomeModel.getPrice());
            if (p > 0)
                mTvPrice.setText("¥" + mHomeModel.getPrice());
            else
                mTvPrice.setVisibility(View.GONE);

            if (TextUtils.isEmpty(mHomeModel.getApp_url()))
                mBtnQuan.setVisibility(View.GONE);
            if (TextUtils.isEmpty(mHomeModel.getQuan_link()))
                mBtnQuan.setVisibility(View.GONE);

        }

        mBtnQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToastUtils.show(mContext,"去领券");
            }
        });

        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToastUtils.show(mContext,"去下单");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
