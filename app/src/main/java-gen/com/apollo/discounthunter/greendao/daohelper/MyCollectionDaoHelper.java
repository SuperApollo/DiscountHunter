package com.apollo.discounthunter.greendao.daohelper;

import com.apollo.discounthunter.base.BaseApplication;
import com.apollo.discounthunter.greendao.bean.MyCollection;
import com.apollo.discounthunter.greendao.dao.DaoSession;
import com.apollo.discounthunter.greendao.dao.MyCollectionDao;

import java.util.List;

/**
 * Created by Apollo on 2017/9/1 11:07
 */

public class MyCollectionDaoHelper {
    private MyCollectionDao myCollectionDao;
    private static volatile MyCollectionDaoHelper myCollectionDaoHelper;

    private MyCollectionDaoHelper() {
        DaoSession daoSession = BaseApplication.getBaseApplication().daoSession;
        myCollectionDao = daoSession.getMyCollectionDao();
    }

    public static MyCollectionDaoHelper getMyCollectionDaoHelper() {
        if (null == myCollectionDaoHelper) {
            synchronized (MyCollectionDaoHelper.class) {
                if (null == myCollectionDaoHelper) {
                    myCollectionDaoHelper = new MyCollectionDaoHelper();
                }
            }
        }
        return myCollectionDaoHelper;
    }

    /**
     * 查出所有
     *
     * @return
     */
    public List<MyCollection> getAllMyCollection() {
        List<MyCollection> myCollections = null;
        try {
            myCollections = myCollectionDao.queryBuilder()
                    .orderDesc(MyCollectionDao.Properties.Release_time)//orderDesc倒序，orderAesc正序
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myCollections;
    }

    public MyCollection getMyCollectionById(String id) {
        try {
            return myCollectionDao.queryBuilder().where(MyCollectionDao.Properties.Id.eq(id)).unique();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删
     */
    public void deleteAllMyCollection() {
        try {
            myCollectionDao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMyCollection(MyCollection myCollection) {
        try {
            myCollectionDao.delete(myCollection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增改
     *
     * @param myCollection
     * @return
     */
    public long insertOrReplace(MyCollection myCollection) {
        try {
            return myCollectionDao.insertOrReplace(myCollection);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
