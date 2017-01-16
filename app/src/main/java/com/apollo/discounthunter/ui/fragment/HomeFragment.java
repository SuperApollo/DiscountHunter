package com.apollo.discounthunter.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.adapter.HomeListAdapter;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.Model;
import com.apollo.discounthunter.retrofit.requestinterface.ApiService;
import com.apollo.discounthunter.ui.activity.GoodsDetailActivity;
import com.apollo.discounthunter.ui.activity.ShowWebActivity;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.widgets.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 主页
 * Created by Apollo on 2017/1/13.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.xlv_home)
    XListView mXlvHome;
    List<Model> mHomeModels = new ArrayList<>();
    private HomeListAdapter mAdapter;
    private static final int STOP_REFRESH = 1;
    private static final int STOP_LOADMORE = 2;
    private int mOffset = 0;
    private boolean firstEnter = true;//首次进入加载一页数据

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_REFRESH:
                    mXlvHome.stopRefresh();
                    break;
                case STOP_LOADMORE:
                    mXlvHome.stopLoadMore();
                    break;
            }
        }
    };

    @Override
    protected void init() {
        mXlvHome.setVerticalScrollBarEnabled(false);
        mXlvHome.setPullLoadEnable(true);
        mXlvHome.setPullRefreshEnable(true);
        mXlvHome.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(STOP_REFRESH, 1000);
                mOffset = 0;
                mHomeModels.clear();
                requestData();
            }

            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(STOP_LOADMORE, 1000);
                requestData();
            }
        });

        mXlvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Model homeModel = mHomeModels.get(i - 1);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.GOODS_INFO, homeModel);
                String appUrl = homeModel.getApp_url();
                if (TextUtils.isEmpty(appUrl)) {//去往weburl
                    bundle.putString(Constants.BUNDLE_TAG, TAG);
                    IntentUtils.sendIntent(getActivity(), ShowWebActivity.class, bundle);
                } else {//去往商品详情页
                    IntentUtils.sendIntent(getActivity(), GoodsDetailActivity.class, bundle);
                }


            }
        });

        mAdapter = new HomeListAdapter(mContext, mHomeModels);
        mXlvHome.setAdapter(mAdapter);
        if (firstEnter) {
            requestData();
            firstEnter = false;
        }


    }

    /**
     * 请求主页列表数据
     */
    private void requestData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> modelCall = service.loadHomeListRepo("API", "app_items", mOffset + "", "10", "0");
        showProgress();
        modelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                mOffset += 10;
                clearProgress();
                mHandler.sendEmptyMessage(STOP_LOADMORE);
                mHandler.sendEmptyMessage(STOP_REFRESH);
                parseData(response);
            }

            @Override
            public void onFailure(Throwable t) {
                clearProgress();
                mHandler.sendEmptyMessage(STOP_LOADMORE);
                mHandler.sendEmptyMessage(STOP_REFRESH);
            }
        });

    }

    /**
     * 解析主页列表数据
     *
     * @param response
     */
    private void parseData(Response<ResponseBody> response) {
        String json = "";
        List<Model> datas;
        try {
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        datas = gson.fromJson(json, new TypeToken<List<Model>>() {
        }.getType());
        if (datas != null)
            mHomeModels.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected boolean hideBottom() {
        return false;
    }
}
