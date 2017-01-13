package com.apollo.discounthunter.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Administrator on 2016/8/24.
 */
public class FragmentUtils {
    public static void replace(FragmentActivity activity, int containerId, Fragment to) {
        replace(activity, containerId, to, null);
    }

    public static void replace(FragmentActivity activity, int containerId, Fragment to, Bundle bundle) {
        FragmentManager manager = activity.getSupportFragmentManager();
        if (bundle != null) {
            to.setArguments(bundle);
        }
        try {
            manager.beginTransaction().replace(containerId, to).commit();
        } catch (Exception e) {
            e.printStackTrace();
            manager.beginTransaction().replace(containerId, to).commitAllowingStateLoss();
        }
    }

    public static void remove(FragmentActivity activity, Fragment fragment) {
        FragmentManager manager = activity.getSupportFragmentManager();
        try {
            manager.beginTransaction().remove(fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
            manager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    public static void addFragment(FragmentActivity activity, int containerId, Fragment fragment) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (!fragment.isAdded()) {
            transaction.addToBackStack(null);
            transaction.add(containerId, fragment);
        }
        try {
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.commitAllowingStateLoss();
        }

    }


}
