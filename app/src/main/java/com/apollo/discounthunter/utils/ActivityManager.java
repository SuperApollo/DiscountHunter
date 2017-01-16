package com.apollo.discounthunter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.apollo.discounthunter.base.BaseApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * 名称：ActivityManager.java
 * <p/>
 * 描述：用于处理退出程序时可以退出所有的activity，而编写的通用类
 */
public class ActivityManager {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static ActivityManager instance;

    private ActivityManager() {
    }

    /**
     * 单例模式中获取唯一的AbActivityManager实例.
     *
     * @return
     */
    public static ActivityManager getInstance() {
        if (null == instance) {
            synchronized (ActivityManager.class) {
                instance = new ActivityManager();
            }
        }
        return instance;
    }

    /**
     * 添加Activity到容器中.
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除Activity从容器中.
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 遍历所有Activity并finish.
     */
    public void removeAllActivity() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 彻底退出程序的方法
     */
    public void exit(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent i = BaseApplication.getContext().getPackageManager()
                            .getLaunchIntentForPackage(BaseApplication.getContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    Process.killProcess(Process.myPid());
                } catch (Exception cep) {
                    cep.printStackTrace();
                }
            }
        }).start();
    }

    public void exitApp() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

}