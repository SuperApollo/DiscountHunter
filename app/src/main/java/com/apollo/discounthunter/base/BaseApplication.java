package com.apollo.discounthunter.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by ${Apollo} on 2017/1/13.
 */

public class BaseApplication extends Application {
    private static BaseApplication baseApplication;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        baseApplication = this;
        mContext = getApplicationContext();
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    public static Context getContext() {
        return mContext;
    }
}
