package com.apollo.discounthunter.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.adapter.HomeListAdapter;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.retrofit.model.Model;
import com.apollo.discounthunter.retrofit.requestinterface.ApiService;
import com.apollo.discounthunter.ui.activity.GoodsDetailActivity;
import com.apollo.discounthunter.ui.activity.MainActivity;
import com.apollo.discounthunter.ui.activity.ShowWebActivity;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.widgets.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 实时热卖
 * Created by Apollo on 2017/1/13.
 */

public class HotFragment extends BaseFragment {
    @BindView(R.id.xlv_hot)
    XListView mXlvHot;
    List<Model> mHotModels = new ArrayList<>();
    private HomeListAdapter mAdapter;
    private static final int STOP_REFRESH = 1;
    private static final int STOP_LOADMORE = 2;
    private final int TOP_BTN_GONE = 3;
    private int mOffset = 0;
    private boolean firstEnter = true;//首次进入加载一页数据

    private int firstVisiblePosition;
    private Button mBtnToTop;
    private Button mBtnToBottom;

    @Override
    protected void init() {
        MainActivity activity = (MainActivity) getActivity();
        mBtnToTop = activity.getmBtnToTop();
        mBtnToBottom = activity.getmBtnToBottom();
        mXlvHot.setVerticalScrollBarEnabled(false);
        mXlvHot.setPullLoadEnable(true);
        mXlvHot.setPullRefreshEnable(true);
        mXlvHot.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(STOP_REFRESH, 1000);
                mOffset = 0;
                mHotModels.clear();
                requestData();
            }

            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(STOP_LOADMORE, 1000);
                requestData();
            }
        });

        mXlvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Model homeModel = mHotModels.get(i - 1);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.GOODS_INFO, homeModel);
                String appUrl = homeModel.getApp_url();
                if (TextUtils.isEmpty(appUrl)) {//去往weburl
                    IntentUtils.sendIntent(getActivity(), ShowWebActivity.class, bundle);
                } else {
                    IntentUtils.sendIntent(getActivity(), GoodsDetailActivity.class, bundle);
                }


            }
        });
        mXlvHot.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                firstVisiblePosition = mXlvHot.getFirstVisiblePosition();
                if (i == 0) {//停止滑动

                } else if (i == 1) {//拖动

                } else if (i == 2) {//惯性
                    if (mBtnToTop.getVisibility() == View.GONE) {
                        mBtnToTop.setVisibility(View.VISIBLE);
                        mBtnToBottom.setVisibility(View.VISIBLE);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        alphaAnimation.setDuration(100);
                        mBtnToTop.startAnimation(alphaAnimation);
                        mBtnToBottom.startAnimation(alphaAnimation);
                        Message message = new Message();
                        message.what = TOP_BTN_GONE;
                        mHandler.sendMessageDelayed(message, 5000);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        mAdapter = new HomeListAdapter(mContext, mHotModels);
        mXlvHot.setAdapter(mAdapter);
        if (firstEnter) {
            requestData();
            firstEnter = false;
        }
        mBtnToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mXlvHot.smoothScrollToPosition(1);
            }
        });
        mBtnToBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHotModels != null && mHotModels.size() > 0)
                    mXlvHot.smoothScrollToPosition(mHotModels.size() - 1);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mXlvHot != null && firstVisiblePosition > 0) {
            mXlvHot.setSelection(firstVisiblePosition);
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
        Call<ResponseBody> modelCall = service.loadHomeListRepo("API", "app_items", mOffset + "", "10", "6");
        if (mHotModels.size() < 1)
            showProgress();
        modelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mOffset += 10;
                clearProgress();
                mHandler.sendEmptyMessage(STOP_LOADMORE);
                mHandler.sendEmptyMessage(STOP_REFRESH);
                parseData(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                clearProgress();
                mToastUtils.show(mContext, "网络错误,请检查网络环境!");
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
        String json;
        List<Model> datas;
        try {
            json = response.body().string();
            Gson gson = new Gson();
            datas = gson.fromJson(json, new TypeToken<List<Model>>() {
            }.getType());
            if (datas != null) {
                mHotModels.addAll(datas);
                mAdapter.notifyDataSetChanged();
            } else {
                mToastUtils.show(mContext, "客官，没有更多了");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mToastUtils.show(mContext, "客官，没有更多了");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected boolean hideBottom() {
        return false;
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case STOP_REFRESH:
                mXlvHot.stopRefresh();
                break;
            case STOP_LOADMORE:
                mXlvHot.stopLoadMore();
                break;
            case TOP_BTN_GONE:
                if (mBtnToTop.getVisibility() == View.VISIBLE) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    alphaAnimation.setDuration(1000);
                    mBtnToTop.startAnimation(alphaAnimation);
                    mBtnToBottom.startAnimation(alphaAnimation);
                    mBtnToTop.setVisibility(View.GONE);
                    mBtnToBottom.setVisibility(View.GONE);
                }
                break;
        }
    }
}
