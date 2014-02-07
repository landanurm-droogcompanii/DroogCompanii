package ru.droogcompanii.application.ui.helpers;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 31.01.14.
 */
public class FragmentRemover {

    public static void removeFragmentByContainerId(final FragmentActivity activity, final int id) {
        new Handler().post(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(id);
                print("By id: ", fragment);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    private static void print(String message, Fragment fragment) {
        LogUtils.debug(message + "fragment == null ? " + (fragment == null));
    }

    public static void removeFragmentByTag(final FragmentActivity activity, final String tag) {
        new Handler().post(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(tag);
                print("By tag: ", fragment);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commitAllowingStateLoss();
            }
        });
    }
}
