package ru.droogcompanii.application.ui.helpers;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by ls on 31.01.14.
 */
public class FragmentRemover {

    public static void removeFragmentByContainerId(final FragmentActivity activity, final int id) {
        new Handler().post(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(id);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    public static void removeFragmentByTag(final FragmentActivity activity, final String tag) {
        new Handler().post(new Runnable() {
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
