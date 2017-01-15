package com.apollo.discounthunter.ui.fragment;

import android.os.Handler;
import android.os.Message;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.adapter.HomeListAdapter;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.HomeModel;
import com.apollo.discounthunter.retrofit.requestinterface.ApiService;
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
 * Created by Apollo on 2017/1/13.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.xlv_home)
    XListView mXlvHome;
    List<HomeModel> mHomeModels = new ArrayList<>();
    private HomeListAdapter mAdapter;
    private static final int STOP_REFRESH = 1;
    private static final int STOP_LOADMORE = 2;
    private int mOffset = 0;

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
            }

            @Override
            public void onLoadMore() {
                requestData();
                mHandler.sendEmptyMessageDelayed(STOP_LOADMORE, 1000);
            }
        });

        mAdapter = new HomeListAdapter(mContext, mHomeModels);
        mXlvHome.setAdapter(mAdapter);
        requestData();

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
        List<HomeModel> datas = null;
        try {
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        datas = gson.fromJson(json, new TypeToken<List<HomeModel>>() {
        }.getType());
        if (datas!=null)
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
