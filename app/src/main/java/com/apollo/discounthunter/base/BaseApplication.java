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
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by ${Apollo} on 2017/1/13.
 */

public class BaseApplication extends Application {
    //各个平台的配置，建议放在全局Application或者程序入口
    {

        PlatformConfig.setWeixin("wx4337fc02d6b46fa0", "86f0416198ffe6ce39b6d1c1031a9283");
        PlatformConfig.setQQZone("1105879293", "etfzk15idzvPMsyX");
        PlatformConfig.setSinaWeibo("1130304914", "68667af335131ba6b3898f3eb31e0d44", "http://www.zhekoulieshou.com");
    }

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
        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        //内存泄漏检测
        LeakCanary.install(this);
        //友盟第三方分享初始化
        UMShareAPI.get(this);
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
