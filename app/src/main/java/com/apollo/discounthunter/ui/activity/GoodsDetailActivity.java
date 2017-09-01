package com.apollo.discounthunter.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.greendao.bean.MyCollection;
import com.apollo.discounthunter.greendao.daohelper.MyCollectionDaoHelper;
import com.apollo.discounthunter.retrofit.model.Model;
import com.apollo.discounthunter.utils.ImageLoaderUtils;
import com.apollo.discounthunter.utils.IntentUtils;

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
    @BindView(R.id.btn_goods_detail_collection)
    Button mBtnCollection;

    private Model mHomeModel;
    private ImageLoaderUtils mImageLoader;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        mImageLoader = ImageLoaderUtils.getInstance(mContext);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mHomeModel = bundle.getParcelable(Constants.GOODS_INFO);
            String title = mHomeModel.getTitle();
            if (!TextUtils.equals("0", mHomeModel.getFlag()))
                title = "【置顶】" + title;
            setTitle(title);
            mTvTitle.setText(title);
            mTvReasson.setText(mHomeModel.getReason());
            mImageLoader.loadImageView(mHomeModel.getPic(), mIv);
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
                if (checkPackage("com.taobao.taobao")) {
                    toTaoBao(mHomeModel.getQuan_link());
                } else {
                    mToastUtils.show(mContext, "请安装淘宝APP");
                    bundle.putString(Constants.BUNDLE_TAG, TAG + "_quan");
                    IntentUtils.sendIntent(GoodsDetailActivity.this, ShowWebActivity.class, bundle);
                }
            }
        });

        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPackage("com.taobao.taobao")) {
                    toTaoBao(mHomeModel.getApp_url());
                } else {
                    mToastUtils.show(mContext, "请安装淘宝APP");
                    bundle.putString(Constants.BUNDLE_TAG, TAG + "_buy");
                    IntentUtils.sendIntent(GoodsDetailActivity.this, ShowWebActivity.class, bundle);
                }

            }
        });
        mBtnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCollection myCollection = new MyCollection(mHomeModel.getId(),
                        mHomeModel.getWeb_url(),
                        mHomeModel.getApp_url(),
                        mHomeModel.getPic(),
                        mHomeModel.getTitle(),
                        mHomeModel.getReason(),
                        mHomeModel.getPrice(),
                        mHomeModel.getSoldcount(),
                        mHomeModel.getCommission(),
                        mHomeModel.getItem_cat_id(),
                        mHomeModel.getNum_iid(),
                        mHomeModel.getPlatform_id(),
                        mHomeModel.getEnd_time(),
                        mHomeModel.getRelease_time(),
                        mHomeModel.getEventid(),
                        mHomeModel.getAddtime(),
                        mHomeModel.getSeller_id(),
                        mHomeModel.getQuan_id(),
                        mHomeModel.getQuan_price(),
                        mHomeModel.getQuan_link(),
                        mHomeModel.getFlag(),
                        mHomeModel.getTotalCount(),
                        mHomeModel.getAppliedCount()
                );
                long result = MyCollectionDaoHelper.getMyCollectionDaoHelper().insertOrReplace(myCollection);
                if (result == -1) {
                    mToastUtils.show(mContext, "收藏失败!");
                } else {
                    mToastUtils.show(mContext, "收藏成功!");
                }


            }
        });

    }

    /**
     * 跳转到淘宝
     *
     * @param url
     */
    private void toTaoBao(String url) {
        if (url.contains("https")) {
            url = url.replace("https", "taobao");
        }
        if (url.contains("http")) {
            url = url.replace("http", "taobao");
        }

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * 检查包名是否存在
     *
     * @param packageName
     * @return
     */
    public boolean checkPackage(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            mContext.getPackageManager().getApplicationInfo(packageName, PackageManager
                    .GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    float startX = 0;
    float startY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();//记录初始值
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float movingX = ev.getRawX();
                float movingY = ev.getRawY();
                if ((movingX - startX) > 200 && Math.abs(movingY - startY) < 100) {//向右滑动200像素，并且 y 方向滑动不超过100像素，判断为结束当前activity手势
                    GoodsDetailActivity.this.finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
