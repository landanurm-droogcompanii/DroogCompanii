package ru.droogcompanii.application.util.ui.fragment;

import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by Leonid on 08.03.14.
 */
public class FragmentUtils {

    public static void show(DialogFragment dialogFragment, FragmentActivity fragmentActivity) {
        String tag = dialogFragment.getClass().getName();
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment previousFragment = fragmentManager.findFragmentByTag(tag);
        if (previousFragment != null) {
            fragmentManager.beginTransaction().remove(previousFragment).commit();
        }
        dialogFragment.show(fragmentManager, tag);
    }

    public static void placeFragmentOnLayout(FragmentActivity fragmentActivity,
                                             int containerId,
                                             Fragment fragment,
                                             String tag) {

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment previousFragment = fragmentManager.findFragmentByTag(tag);
        if (previousFragment != null) {
            transaction.remove(previousFragment);
        }
        transaction.add(containerId, fragment, tag);
        transaction.commitAllowingStateLoss();
    }


    public static void removeFragmentByTag(final FragmentActivity activity, final String tag) {
        tryPost(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    return;
                }
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
}
