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
        tryPost(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(id);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    private static void tryPost(final Runnable runnable) {
        new Handler().post(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    LogUtils.debug(e.getMessage());
                }
            }
        });
    }

    public static void removeFragmentByTag(final FragmentActivity activity, final String tag) {
        tryPost(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(tag);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commitAllowingStateLoss();
            }
        });
    }
}
