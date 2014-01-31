package ru.droogcompanii.application.ui.helpers;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by ls on 31.01.14.
 */
public class TaskFragmentRemover {

    public static void remove(final FragmentActivity fragmentActivity, final int fragmentId) {
        new Handler().post(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                Fragment taskFragment = fragmentManager.findFragmentById(fragmentId);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(taskFragment);
                transaction.commitAllowingStateLoss();
            }
        });
    }
}
