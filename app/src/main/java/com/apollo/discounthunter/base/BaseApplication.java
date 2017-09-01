package com.apollo.discounthunter.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.greendao.dao.DaoMaster;
import com.apollo.discounthunter.greendao.dao.DaoSession;
import com.apollo.discounthunter.utils.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ${Apollo} on 2017/1/13.
 */

public class BaseApplication extends Application {
    private static BaseApplication baseApplication;
    private static Context mContext;
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initDao();
    }

    private void init() {
        baseApplication = this;
        mContext = getApplicationContext();
        MobclickAgent.setCatchUncaughtExceptions(false);//关闭友盟错误统计
        //初始化错误日志记录
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        //内存泄漏检测
        LeakCanary.install(this);
    }

    /**
     * 初始化数据库
     */
    private void initDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, AppConfig.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        daoSession = master.newSession();
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    public static Context getContext() {
        return mContext;
    }
}
