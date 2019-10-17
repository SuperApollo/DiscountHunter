package com.apollo.discounthunter.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.apollo.discounthunter.BuildConfig;
import com.apollo.discounthunter.constants.AppConfig;
import com.apollo.discounthunter.greendao.dao.DaoMaster;
import com.apollo.discounthunter.greendao.dao.DaoSession;
import com.apollo.discounthunter.utils.CrashHandler;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

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
        initSwipe();
        initBaichuan();
    }

    /**
     * 初始化阿里百川
     */
    private void initBaichuan() {
//        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
//            @Override
//            public void onSuccess() {
//                //初始化成功，设置相关的全局配置参数
//                ToastUtils.show("百川初始化成功");
//                LogUtil.i("apollo","百川初始化成功");
//                // ...
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
//                ToastUtils.show("百川初始化失败："+msg);
//                LogUtil.e("apollo","初始化失败："+code+":"+msg);
//            }
//        });
    }

    private void initSwipe() {
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null);
    }

    private void init() {
        baseApplication = this;
        mContext = getApplicationContext();
        MobclickAgent.setCatchUncaughtExceptions(false);//关闭友盟错误统计
        //初始化错误日志记录
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        initLeakCanary();
        //友盟第三方分享初始化
        UMShareAPI.get(this);
        //log工具
        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
    }

    /**
     * 内存泄漏检测
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
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
