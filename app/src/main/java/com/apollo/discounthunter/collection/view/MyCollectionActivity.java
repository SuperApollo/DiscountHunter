package com.apollo.discounthunter.collection.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.collection.presenter.PresenterImpl;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.greendao.bean.MyCollection;
import com.apollo.discounthunter.retrofit.model.Model;
import com.apollo.discounthunter.ui.activity.BaseActivity;
import com.apollo.discounthunter.ui.activity.GoodsDetailActivity;
import com.apollo.discounthunter.ui.activity.ShowWebActivity;
import com.apollo.discounthunter.utils.IntentUtils;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Apollo on 2017/8/31 15:59
 */

public class MyCollectionActivity extends BaseActivity implements ICollectionView, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    @BindView(R.id.lv_collection)
    ListView mLvCollection;
    @BindView(R.id.tv_collection_none)
    TextView mTvCollectionNone;
    private MyCollectionAdapter mAdapter;
    private PresenterImpl mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected int getMenuLayoutId() {
        return -1;
    }

    @Override
    protected void initView(View view) {
        setTitle("我的收藏");
        mLvCollection.setOnItemClickListener(this);
        mLvCollection.setOnItemLongClickListener(this);
        mPresenter = new PresenterImpl(this);
        mPresenter.getAllCollections();
    }

    @Override
    public void showData(List<MyCollection> datas) {
        if (datas != null && datas.size() > 0) {
            mTvCollectionNone.setVisibility(View.GONE);
            mAdapter = new MyCollectionAdapter(mContext, datas);
            mLvCollection.setAdapter(mAdapter);
        } else {
            mTvCollectionNone.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void refreshView(List<MyCollection> datas) {
        if (datas != null && datas.size() > 0) {
            mTvCollectionNone.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        } else {
            mTvCollectionNone.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyCollection myCollection = mAdapter.getDatas().get(i);
        //利用gson转换对象
        Gson gson = new Gson();
        String json = gson.toJson(myCollection);
        Model model = gson.fromJson(json, Model.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.GOODS_INFO, model);
        String appUrl = model.getApp_url();
        if (TextUtils.isEmpty(appUrl)) {//去往weburl
            bundle.putString(Constants.BUNDLE_TAG, TAG);
            IntentUtils.sendIntent(MyCollectionActivity.this, ShowWebActivity.class, bundle);
        } else {//去往商品详情页
            IntentUtils.sendIntent(MyCollectionActivity.this, GoodsDetailActivity.class, bundle);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyCollectionActivity.this);
        builder.setMessage("您确定要删除该条收藏吗？")
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        MyCollection myCollection = mAdapter.getDatas().get(position);
                        mPresenter.deleteCollection(myCollection);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();

        return true;//消费长按事件
    }
}
