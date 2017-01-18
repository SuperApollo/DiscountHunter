package com.apollo.discounthunter.greendao.daohelper;

import com.apollo.discounthunter.base.BaseApplication;
import com.apollo.discounthunter.greendao.bean.SearchHistory;
import com.apollo.discounthunter.greendao.dao.DaoSession;
import com.apollo.discounthunter.greendao.dao.SearchHistoryDao;

import java.util.List;

/**
 * Created by Apollo on 2017/1/18.
 */

public class SearchHistoryDaoHelper {
    private SearchHistoryDao searchHistoryDao;
    private static SearchHistoryDaoHelper searchHistoryDaoHelper;

    private SearchHistoryDaoHelper() {
        DaoSession daoSession = BaseApplication.getBaseApplication().daoSession;
        searchHistoryDao = daoSession.getSearchHistoryDao();
    }

    public static SearchHistoryDaoHelper getSearchHistoryDaoHelper() {
        if (null == searchHistoryDaoHelper) {
            synchronized (SearchHistoryDaoHelper.class) {
                if (null == searchHistoryDaoHelper) {
                    searchHistoryDaoHelper = new SearchHistoryDaoHelper();
                }
            }
        }
        return searchHistoryDaoHelper;
    }

    /**
     * 查出所有
     *
     * @return
     */
    public List<SearchHistory> getAllSearchHistory() {
        List<SearchHistory> searchHistories = null;
        try {
            searchHistories = searchHistoryDao.queryBuilder()
                    .orderDesc(SearchHistoryDao.Properties.Time)//orderDesc倒序，orderAesc正序
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchHistories;
    }

    /**
     * 删
     */
    public void deleteAllSearchHistory() {
        try {
            searchHistoryDao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增改
     *
     * @param searchHistory
     * @return
     */
    public long insertOrReplace(SearchHistory searchHistory) {
        try {
            return searchHistoryDao.insertOrReplace(searchHistory);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 查出一个个人信息
     *
     * @return
     */
    public SearchHistory getSearchHistoryByText(String text) {
        SearchHistory searchHistory = searchHistoryDao.queryBuilder().where(SearchHistoryDao.Properties.Search.eq(text)).unique();
        return searchHistory;
    }


}
