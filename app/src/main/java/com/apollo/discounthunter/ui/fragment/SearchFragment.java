package com.apollo.discounthunter.ui.fragment;

import com.apollo.discounthunter.R;
import com.apollo.discounthunter.adapter.HistoryAdapter;
import com.apollo.discounthunter.greendao.bean.SearchHistory;
import com.apollo.discounthunter.greendao.daohelper.SearchHistoryDaoHelper;
import com.apollo.discounthunter.ui.activity.MainActivity;
import com.apollo.discounthunter.utils.TimeUtils;
import com.apollo.discounthunter.widgets.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 搜索界面
 * Created by Apollo on 2017/1/17.
 */

public class SearchFragment extends BaseFragment {
    @BindView(R.id.xlv_search)
    XListView mXlvSearch;
    private List<SearchHistory> mSearchHistories = new ArrayList<>();
    private HistoryAdapter mHistoryAdapter;

    //此方法在懒加载中不走
    @Override
    protected void init() {
    }

    @Override
    public void onResume() {
        super.onResume();
        searchHistory();
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnSearchListner(new MainActivity.OnSearchListner() {
            @Override
            public boolean onSearchTextSubmit(String text) {
                searchGoods(text);
                return true;
            }

            @Override
            public boolean onSearchTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * 搜索商品
     *
     * @param text
     */
    private void searchGoods(String text) {
        //保存搜索历史
        SearchHistoryDaoHelper helper = SearchHistoryDaoHelper.getSearchHistoryDaoHelper();
        helper.insertOrReplace(new SearchHistory(text, TimeUtils.getCurrentTime()));
        //搜索


    }

    /**
     * 搜索历史
     */
    private void searchHistory() {
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
}
