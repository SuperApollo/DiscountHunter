package com.apollo.discounthunter.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.greendao.bean.MyCollection;
import com.apollo.discounthunter.greendao.daohelper.MyCollectionDaoHelper;
import com.apollo.discounthunter.retrofit.model.Model;
import com.apollo.discounthunter.utils.ImageLoaderUtils;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.utils.ToastUtils;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

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
            ToastUtils.show("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.show("分享失败" + t.getMessage());
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.show("分享取消");
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected int getMenuLayoutId() {
        return R.menu.goods_detail;
    }

    @Override
    protected void initView(View view) {
        mImageLoader = ImageLoaderUtils.getInstance(mContext);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mHomeModel = bundle.getParcelable(Constants.GOODS_INFO);
            String title = mHomeModel.getTitle();
            if (!TextUtils.equals("0", mHomeModel.getFlag())) {
                title = "【置顶】" + title;
            }
            setTitle(title);
            mTvTitle.setText(title);
            mTvReasson.setText(mHomeModel.getReason());
            mImageLoader.loadImageView(mHomeModel.getPic(), mIv);
            float p = Float.parseFloat(mHomeModel.getPrice());
            if (p > 0) {
                mTvPrice.setText("¥" + mHomeModel.getPrice());
            } else {
                mTvPrice.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(mHomeModel.getApp_url())) {
                mBtnQuan.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(mHomeModel.getQuan_link())) {
                mBtnQuan.setVisibility(View.GONE);
            }

        }

        mBtnQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPackage("com.taobao.taobao")) {
                    toTaoBao(mHomeModel.getQuan_link());
                } else {
                    ToastUtils.show(mContext, "请安装淘宝APP");
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
                    ToastUtils.show(mContext, "请安装淘宝APP");
                    bundle.putString(Constants.BUNDLE_TAG, TAG + "_buy");
                    IntentUtils.sendIntent(GoodsDetailActivity.this, ShowWebActivity.class, bundle);
                }

            }
        });
        mBtnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCollection myCollectionById = MyCollectionDaoHelper.getMyCollectionDaoHelper().getMyCollectionById(mHomeModel.getId());
                if (myCollectionById != null) {
                    ToastUtils.show(mContext, "您已经收藏过该宝贝!");
                    return;
                }
                Gson gson = new Gson();
                String json = gson.toJson(mHomeModel);
                MyCollection myCollection = gson.fromJson(json, MyCollection.class);
                long result = MyCollectionDaoHelper.getMyCollectionDaoHelper().insertOrReplace(myCollection);
                if (result == -1) {
                    ToastUtils.show(mContext, "收藏失败!");
                } else {
                    ToastUtils.show(mContext, "收藏成功!");
                }


            }
        });

    }

    @Override
    protected void handleMsg(Message msg) {

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
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
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
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startX = ev.getRawX();//记录初始值
//                startY = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float movingX = ev.getRawX();
//                float movingY = ev.getRawY();
//                if ((movingX - startX) > 200 && Math.abs(movingY - startY) < 100) {//向右滑动200像素，并且 y 方向滑动不超过100像素，判断为结束当前activity手势
//                    GoodsDetailActivity.this.finish();
//                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * 分享商品详情给朋友
     */
    private void share() {
        String url = mHomeModel.getQuan_link();
        if (url.contains("https")) {
            url.replace("https", "http");
        }
        UMWeb web = new UMWeb(url);
        UMImage thumb = new UMImage(mContext, mHomeModel.getPic());
        web.setTitle("好货推荐");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("我发现了一个物美价廉的宝贝，分享给你哦");//描述
        mShareAction = new ShareAction(GoodsDetailActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //友盟分享内存泄露
        UMShareAPI.get(this).release();
    }
}
