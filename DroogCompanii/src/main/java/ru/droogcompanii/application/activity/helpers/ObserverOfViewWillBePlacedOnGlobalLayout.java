package ru.droogcompanii.application.activity.helpers;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by ls on 23.12.13.
 */
public class ObserverOfViewWillBePlacedOnGlobalLayout {

    public static void runAfterViewWillBePlacedOnLayout(final View view, final Runnable runnable) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver == null) {
            return;
        }
        if (!viewTreeObserver.isAlive()) {
            return;
        }
        viewTreeObserver.addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    removeOnGlobalLayoutListener(view, this);
                    runnable.run();
                }
            }
        );
    }

    @SuppressWarnings("deprecation")
    private static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 16) {
            viewTreeObserver.removeGlobalOnLayoutListener(listener);
        } else {
            viewTreeObserver.removeOnGlobalLayoutListener(listener);
        }
    }
}
