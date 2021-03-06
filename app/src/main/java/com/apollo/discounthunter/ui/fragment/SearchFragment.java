package com.apollo.discounthunter.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.adapter.HistoryAdapter;
import com.apollo.discounthunter.adapter.HomeListAdapter;
import com.apollo.discounthunter.constants.Constants;
import com.apollo.discounthunter.greendao.bean.SearchHistory;
import com.apollo.discounthunter.greendao.daohelper.SearchHistoryDaoHelper;
import com.apollo.discounthunter.retrofit.model.Model;
import com.apollo.discounthunter.retrofit.requestinterface.ApiService;
import com.apollo.discounthunter.ui.activity.GoodsDetailActivity;
import com.apollo.discounthunter.ui.activity.MainActivity;
import com.apollo.discounthunter.ui.activity.ShowWebActivity;
import com.apollo.discounthunter.utils.IntentUtils;
import com.apollo.discounthunter.utils.TimeUtils;
import com.apollo.discounthunter.widgets.XListView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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
 * 搜索界面
 * Created by Apollo on 2017/1/17.
 */

public class SearchFragment extends BaseFragment {
    @BindView(R.id.xlv_search)
    XListView mXlvSearch;
    @BindView(R.id.rl_search_history)
    RelativeLayout mRlSearchHistory;
    @BindView(R.id.btn_clear_search_history)
    Button mBtnClear;
    private List<SearchHistory> mSearchHistories = new ArrayList<>();
    private List<Model> mSearchModels = new ArrayList<>();
    private HistoryAdapter mHistoryAdapter;
    private int mOffset = 0;
    private static final int STOP_REFRESH = 1;
    private static final int STOP_LOADMORE = 2;
    private final int TOP_BTN_GONE = 3;

    private HomeListAdapter mSearchAdapter;
    private String mSearchText;
    private boolean isHistory;//当前展示的是否是搜索历史的adapter内容
    private String eId;
    private boolean searchChange;
    private Button mBtnToTop;
    private Button mBtnToBottom;

    //此方法在懒加载中不走
    @Override
    protected void init() {
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchHistory();
        MainActivity activity = (MainActivity) getActivity();
        mBtnToTop = activity.getmBtnToTop();
        mBtnToBottom = activity.getmBtnToBottom();
        if (mSearchModels == null || mSearchModels.size() < 1) {
            mBtnToTop.setVisibility(View.GONE);
            mBtnToBottom.setVisibility(View.GONE);
        }
        eId = activity.getmEid();
        activity.setOnSearchListner(new MainActivity.OnSearchListner() {
            @Override
            public boolean onSearchTextSubmit(String text) {
                mSearchText = text;
                searchGoods(text);
                //保存搜索历史
                SearchHistoryDaoHelper helper = SearchHistoryDaoHelper.getSearchHistoryDaoHelper();
                helper.insertOrReplace(new SearchHistory(text, TimeUtils.getCurrentTime()));

                return true;
            }

            @Override
            public boolean onSearchTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) && !TextUtils.equals(newText, mSearchText)) {//若搜索关键字变化，则清空集合
                    searchChange = true;
                } else {
                    searchChange = false;
                }
                return true;
            }
        });

        mXlvSearch.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                searchChange = false;
                mHandler.sendEmptyMessageDelayed(STOP_REFRESH, 1000);
                mOffset = 0;
                mSearchModels.clear();
                searchGoods(mSearchText);
            }

            @Override
            public void onLoadMore() {
                searchChange = false;
                mHandler.sendEmptyMessageDelayed(STOP_LOADMORE, 1000);
                searchGoods(mSearchText);
            }
        });

        mXlvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i > 0 ? i - 1 : 0;
                if (isHistory) {//当前是搜索历史列表，点击后搜索
                    SearchHistory searchHistory = mSearchHistories.get(position);
                    searchGoods(searchHistory.getSearch());
                } else {//当前是搜索结果列表，点击后进入详情
                    Model searchModel = mSearchModels.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.GOODS_INFO, searchModel);
                    String appUrl = searchModel.getApp_url();
                    if (TextUtils.isEmpty(appUrl)) {//去往weburl
                        bundle.putString(Constants.BUNDLE_TAG, TAG);
                        IntentUtils.sendIntent(getActivity(), ShowWebActivity.class, bundle);
                    } else {//去往商品详情页
                        IntentUtils.sendIntent(getActivity(), GoodsDetailActivity.class, bundle);
                    }
                }
            }
        });

        mXlvSearch.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
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

        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("您确定要清空搜索历史吗？")
                        .setTitle("提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                mSearchHistories.clear();
                                mHistoryAdapter.notifyDataSetChanged();
                                SearchHistoryDaoHelper helper = SearchHistoryDaoHelper.getSearchHistoryDaoHelper();
                                helper.deleteAllSearchHistory();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

        mBtnToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mXlvSearch.smoothScrollToPosition(1);
            }
        });

        mBtnToBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchModels != null && mSearchModels.size() > 0)
                    mXlvSearch.smoothScrollToPosition(mSearchModels.size() - 1);
            }
        });
    }

    /**
     * 搜索商品
     *
     * @param text
     */
    private void searchGoods(String text) {
        if (mRlSearchHistory.getVisibility() == View.VISIBLE)
            mRlSearchHistory.setVisibility(View.GONE);

        mXlvSearch.setPullLoadEnable(true);
        mXlvSearch.setPullRefreshEnable(true);

        //搜索
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> searchlCall = service.loadSearchListRepo("API", "app_items", mOffset + "", "10", eId, text);
        showProgress();
        searchlCall.enqueue(new Callback<ResponseBody>() {
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
     * 解析数据
     *
     * @param response
     */
    private void parseData(Response<ResponseBody> response) {
        String json = "";
        List<Model> datas = null;
        try {
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        try {
            datas = gson.fromJson(json, new TypeToken<List<Model>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            mToastUtils.show(mContext, "抱歉，未索索到相关商品");
            e.printStackTrace();
        }
        int index = mSearchModels.size();
        if (datas != null && datas.size() > 1) {
            if (!searchChange) {//如果搜索内容没变化则添加集合
                mSearchModels.addAll(datas);
            } else {//如果搜索内容变化了，则添加集合清空之前的搜索结果,为了解决mSearchModels.clear 报错
                mSearchModels.addAll(datas);
                for (int i = 0; i < index; i++) {
                    mSearchModels.remove(0);
                }

            }
        } else {
            mToastUtils.show(mContext, "抱歉，未索索到相关商品");
            return;
        }

        if (mSearchAdapter == null) {
            mSearchAdapter = new HomeListAdapter(mContext, mSearchModels);
            mXlvSearch.setAdapter(mSearchAdapter);
        } else {
            mSearchAdapter.notifyDataSetChanged();
            MainActivity fatherActivity = (MainActivity) getActivity();
            EditText editText = fatherActivity.getmSearchEditText();
            if (editText != null) {//上拉加载后让搜索edittext失去焦点，防止软键盘弹出
                editText.clearFocus();
            }
        }
        isHistory = false;

    }

    /**
     * 搜索历史
     */
    private void searchHistory() {
        isHistory = true;
        SearchHistoryDaoHelper helper = SearchHistoryDaoHelper.getSearchHistoryDaoHelper();
        mSearchHistories = helper.getAllSearchHistory();
        mHistoryAdapter = new HistoryAdapter(mSearchHistories, mContext);
        mXlvSearch.setAdapter(mHistoryAdapter);
        mXlvSearch.setPullLoadEnable(false);
        mXlvSearch.setPullRefreshEnable(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected boolean hideBottom() {
        return true;
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case STOP_REFRESH:
                mXlvSearch.stopRefresh();
                break;
            case STOP_LOADMORE:
                mXlvSearch.stopLoadMore();
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
